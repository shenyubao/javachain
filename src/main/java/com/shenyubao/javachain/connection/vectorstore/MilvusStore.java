package com.shenyubao.javachain.connection.vectorstore;

import com.shenyubao.javachain.CommonError;
import com.shenyubao.javachain.JavaChainException;
import com.shenyubao.javachain.llms.chatclient.embedding.Embedding;
import com.shenyubao.javachain.connection.retriever.Document;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.*;
import io.milvus.param.*;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.DescribeCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shenyubao
 * @date 2023/6/26 16:45
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class MilvusStore extends VectorStore {


    private String apiKey;
    private String endpoint;

    private String collectionName = "javachain";
    private Integer dimension = 1536;

    private MilvusServiceClient milvusClient;

    private FieldType fieldId = FieldType.newBuilder().withName("id").withDataType(DataType.Int64).withPrimaryKey(true).withAutoID(true).build();
    private FieldType fieldDatasetId = FieldType.newBuilder().withName("datasetId").withDataType(DataType.VarChar).withMaxLength(512).build();
    private FieldType fieldEmbedding = FieldType.newBuilder().withName("embedding").withDataType(DataType.FloatVector).withDimension(dimension).build();
    private FieldType fieldRowContent = FieldType.newBuilder().withName("rowContent").withDataType(DataType.VarChar).withMaxLength(4096).build();

    public MilvusStore(String endpoint, String apiKey) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.milvusClient = getClient();
    }

    @Override
    public void init() {
        Boolean collectionExist = isCollectionExist();
        if (!collectionExist) {
            checkAndCreateCollection();
            createIndex();
            loadCollection();
        }
    }

    private MilvusServiceClient getClient() {
        final MilvusServiceClient milvusClient = new MilvusServiceClient(
                ConnectParam.newBuilder()
                        .withUri(endpoint)
                        .withToken(apiKey)
                        .build());
        log.info("[VECTOR] Connecting to Milvus DB: " + endpoint);
        return milvusClient;
    }

    protected Boolean isCollectionExist() {
        R<DescribeCollectionResponse> responseR =
                milvusClient.describeCollection(DescribeCollectionParam.newBuilder().withCollectionName(collectionName).build());
        return responseR.getData() != null;
    }

    private void checkAndCreateCollection() {
        CreateCollectionParam createCollectionParam = CreateCollectionParam
                .newBuilder()
                .withCollectionName(collectionName)
                .addFieldType(fieldId).addFieldType(fieldDatasetId).addFieldType(fieldEmbedding).addFieldType(fieldRowContent)
                .build();

        log.info("[VECTOR] Create Milvus Collection: {}, Schema:{}", collectionName, createCollectionParam);
        R<RpcStatus> rpcStatusR = milvusClient.createCollection(createCollectionParam);
        if (rpcStatusR.getStatus().equals(R.Status.Success.getCode())) {
            log.info("[VECTOR] Create Milvus Success");
        } else {
            log.error("[VECTOR] Create Milvus Failed:{}", rpcStatusR.getMessage());
            throw new JavaChainException(CommonError.SYS_ERROR, "on create milvus collection");
        }
    }

    private void createIndex() {
        log.info("[VECTOR] Building AutoIndex...");
        final IndexType INDEX_TYPE = IndexType.AUTOINDEX;   // IndexType
        long startIndexTime = System.currentTimeMillis();
        R<RpcStatus> indexR = milvusClient.createIndex(
                CreateIndexParam.newBuilder()
                        .withCollectionName(collectionName)
                        .withFieldName(fieldEmbedding.getName())
                        .withIndexType(INDEX_TYPE)
                        .withMetricType(MetricType.IP)
                        .withSyncMode(Boolean.TRUE)
                        .withSyncWaitingInterval(500L)
                        .withSyncWaitingTimeout(30L)
                        .build());
        log.info(String.format("[VECTOR] Building AutoIndex result: %s", indexR));
    }

    private void loadCollection() {
        log.info("[VECTOR] Loading Collection...");
        R<RpcStatus> rpcStatusR = milvusClient.loadCollection(LoadCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withSyncLoad(true)
                .withSyncLoadWaitingInterval(500L)
                .withSyncLoadWaitingTimeout(100L)
                .build());
        log.info(String.format("[VECTOR] Loading Collection result: %s", rpcStatusR));
    }

    @Override
    public List<String> addDocuments(List<Document> documents) {
        List<InsertParam.Field> fields = new ArrayList<>();
        List<String> listDatasetId = new ArrayList<>();
        List<List<Float>> listEmbedding = new ArrayList<>();
        List<String> listRowContent = new ArrayList<>();

        for (Document document : documents) {
            // 向量化
            if (document.getEmbedding() == null) {
                document.setEmbedding(getEmbedding().embedQuery(document.getPageContent()).getEmbedding());
            } else {
                document.setEmbedding(document.getEmbedding());
            }

            listEmbedding.add(document.getEmbedding());
            listDatasetId.add(document.getDatasetID());
            listRowContent.add(document.getPageContent());
        }

        fields.add(new InsertParam.Field(fieldDatasetId.getName(), listDatasetId));
        fields.add(new InsertParam.Field(fieldEmbedding.getName(), listEmbedding));
        fields.add(new InsertParam.Field(fieldRowContent.getName(), listRowContent));

        InsertParam insertParam = InsertParam.newBuilder()
                .withCollectionName(collectionName)
                .withFields(fields)
                .build();
        R<MutationResult> insertR = milvusClient.insert(insertParam);
        if (insertR.getStatus().equals(R.Status.Success.getCode())) {
            log.info("[VECTOR] Insert Documents Success, size:{}", documents.size());
            List<Long> ids = insertR.getData().getIDs().getIntId().getDataList();
            return ids.stream().map(Object::toString).collect(Collectors.toList());

        } else {
            log.error("[VECTOR] Insert Documents Failed:{}", insertR.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean removeDocuments(List<String> documentIds) {
        String DOCUMENT_EXPR = String.join(",", documentIds);
        String DELETE_EXPR = String.format("%s in [%s]", fieldId.getName(), DOCUMENT_EXPR);
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withCollectionName(collectionName)
                .withExpr(DELETE_EXPR)
                .build();
        R<MutationResult> deleteR = milvusClient.delete(deleteParam);
        log.info("[VECTOR] Remove Documents:{}", deleteR);
        return deleteR.getStatus().equals(R.Status.Success.getCode());
    }

    @Override
    public List<Document> similaritySearch(String datasetId, String query, int SEARCH_K) {
        Embedding embedding = getEmbedding().embedQuery(query);
        List<String> search_output_fields = Arrays.asList(fieldId.getName(), fieldRowContent.getName());
        log.info("[VECTOR] Searching: " + query);

        String expr = String.format("%s == \"%s\"", fieldDatasetId.getName(), datasetId);

        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName(collectionName)
                .withMetricType(MetricType.IP)
                .withOutFields(search_output_fields)
                .withTopK(SEARCH_K)
                .withVectors(Collections.singletonList(embedding.getEmbedding()))
                .withVectorFieldName(fieldEmbedding.getName())
                .withExpr(expr)
                .build();

        R<SearchResults> search = milvusClient.search(searchParam);
        if (search.getStatus() != R.Status.Success.getCode()) {
            throw new JavaChainException(CommonError.VECTOR_ERROR, search.getMessage());
        }

        if (search.getData() == null || search.getData().getResults() == null) {
            return new ArrayList<>();
        }

        log.info("[VECTOR] Searching Result: " + search.getData().getResults().getFieldsDataList());
        List<Long> listIdx = new ArrayList<>();
        List<String> listData = new ArrayList<>();
        for (FieldData fieldData : search.getData().getResults().getFieldsDataList()) {
            if (fieldData.getFieldName().equals(fieldId.getName())) {
                listIdx.addAll(fieldData.getScalars().getLongData().getDataList());
            }
            if (fieldData.getFieldName().equals(fieldRowContent.getName())) {
                listData.addAll(fieldData.getScalars().getStringData().getDataList());
            }
        }

        return IntStream.range(0, listIdx.size())
                .mapToObj(i -> new Document(listIdx.get(i).toString(), listData.get(i)))
                .collect(Collectors.toList());
    }
}

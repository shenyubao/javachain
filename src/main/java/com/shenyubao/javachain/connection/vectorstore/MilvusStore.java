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
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private FieldType fieldContentId = FieldType.newBuilder().withName("contentId").withDataType(DataType.VarChar).withMaxLength(512).build();
    private FieldType fieldType = FieldType.newBuilder().withName("type").withDataType(DataType.Int32).build();
    private FieldType fieldContent = FieldType.newBuilder().withName("content").withDataType(DataType.FloatVector).withDimension(dimension).build();
    private FieldType fieldIdx = FieldType.newBuilder().withName("idx").withDataType(DataType.Int32).build();
    private FieldType fieldRowContent = FieldType.newBuilder().withName("rowContent").withDataType(DataType.VarChar).withMaxLength(4096).build();

    public MilvusStore(String endpoint, String apiKey) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.milvusClient = getClient();
    }

    @Override
    public void init() {
        Boolean collectionExist = isCollectionExist();
        if (!collectionExist){
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
                .addFieldType(fieldId).addFieldType(fieldContentId).addFieldType(fieldType).addFieldType(fieldContent).addFieldType(fieldIdx).addFieldType(fieldRowContent)
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
                        .withFieldName(fieldContent.getName())
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
    public void addDocuments(List<Document> documents) {
        List<InsertParam.Field> fields = new ArrayList<>();
        List<String> listContentID = new ArrayList<>();
        List<Integer> listType = new ArrayList<>();
        List<List<Float>> listContent = new ArrayList<>();
        List<Integer> listIdx = new ArrayList<>();
        List<String> listRowContent = new ArrayList<>();

        for (Document document : documents) {
            listContentID.add(document.getUniqueId());
            listContent.add(document.getEmbedding());
            listIdx.add(document.getIndex());
            listRowContent.add(document.getPageContent());
            listType.add(1);
        }

        fields.add(new InsertParam.Field(fieldContentId.getName(), listContentID));
        fields.add(new InsertParam.Field(fieldType.getName(), listType));
        fields.add(new InsertParam.Field(fieldContent.getName(), listContent));
        fields.add(new InsertParam.Field(fieldIdx.getName(), listIdx));
        fields.add(new InsertParam.Field(fieldRowContent.getName(), listRowContent));

        InsertParam insertParam = InsertParam.newBuilder()
                .withCollectionName(collectionName)
                .withFields(fields)
                .build();
        R<MutationResult> insertR = milvusClient.insert(insertParam);
        if (insertR.getStatus().equals(R.Status.Success.getCode())) {
            log.info("[VECTOR] Insert Documents Success, size:{}", documents.size());
        } else {
            log.error("[VECTOR] Create Milvus Failed:{}", insertR.getMessage());
        }
    }

    @Override
    public List<Document> similaritySearch(String query, int SEARCH_K) {
        Embedding embedding = getEmbedding().embedQuery(query);
        List<String> search_output_fields = Arrays.asList(fieldIdx.getName(), fieldRowContent.getName());
        log.info("[VECTOR] Searching: " + query);

//        String SEARCH_PARAM= "";

        SearchParam searchParam = SearchParam.newBuilder()
                .withCollectionName(collectionName)
                .withMetricType(MetricType.IP)
                .withOutFields(search_output_fields)
                .withTopK(SEARCH_K)
                .withVectors(Collections.singletonList(embedding.getEmbedding()))
                .withVectorFieldName(fieldContent.getName())
                .build();

        R<SearchResults> search = milvusClient.search(searchParam);
        if (search.getStatus() != R.Status.Success.getCode()) {
            throw new JavaChainException(CommonError.VECTOR_ERROR, search.getMessage());
        }

        if (search.getData() == null || search.getData().getResults() == null) {
            return new ArrayList<>();
        }

        log.info("[VECTOR] Searching Result: " + search.getData().getResults().getFieldsDataList());
        List<Integer> listIdx = new ArrayList<>();
        List<String> listData = new ArrayList<>();
        for (FieldData fieldData : search.getData().getResults().getFieldsDataList()) {
            if (fieldData.getFieldName().equals(fieldIdx.getName())) {
                listIdx.addAll(fieldData.getScalars().getIntData().getDataList());
            }
            if (fieldData.getFieldName().equals(fieldRowContent.getName())) {
                listData.addAll(fieldData.getScalars().getStringData().getDataList());
            }
        }

        return IntStream.range(0, listIdx.size())
                .mapToObj(i -> new Document(listIdx.get(i), listData.get(i)))
                .collect(Collectors.toList());
    }
}

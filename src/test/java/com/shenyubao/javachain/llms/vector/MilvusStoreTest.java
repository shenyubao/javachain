package com.shenyubao.javachain.llms.vector;

import com.shenyubao.javachain.chain.extend.RetrievalChain;
import com.shenyubao.javachain.connection.embeddings.OpenAIEmbeddings;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.vectorstore.MilvusStore;
import com.shenyubao.javachain.utils.PropertiesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/26 23:11
 */
public class MilvusStoreTest {
    private String milvus_apiKey;
    private String milvus_endpoint;
    private String endpoint;
    private String apiKey;

    @BeforeEach
    void setUp() {
        PropertiesUtils propertiesUtils = new PropertiesUtils("javachain");

        this.endpoint = propertiesUtils.get("openai.endpoint");
        this.apiKey = propertiesUtils.get("openai.apikey");
        this.milvus_endpoint = propertiesUtils.get("milvus_endpoint");
        this.milvus_apiKey = propertiesUtils.get("milvus_apikey");
    }

    @Test
    public void embedding_test() {

        //数据库知识库向量持久化
        MilvusStore milvusStore = new MilvusStore(milvus_endpoint,milvus_apiKey);
        milvusStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
        milvusStore.init();


        String datasetId = "10000";
        int topK = 2;
        List<String> knowledge = Arrays.asList(
                "百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123",
                "openapi接口传数据是有大小限制在10M以内，这个是nginx限制的要求");
        milvusStore.addTexts(knowledge,datasetId);

        List<Document> documents = milvusStore.similaritySearch(datasetId,"OPENAPI有接口大小限制不，有的话是多少",topK);
        Assertions.assertEquals(2, documents.size());

        milvusStore.removeDocuments(documents.stream().map(Document::getId).collect(Collectors.toList()));
        documents = milvusStore.similaritySearch(datasetId,"OPENAPI有接口大小限制不，有的话是多少",topK);
        Assertions.assertEquals(0, documents.size());
    }
}

package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.chain.retrievalqa.RetrievalQA;
import com.shenyubao.javachain.embeddings.OpenAIEmbeddings;
import com.shenyubao.javachain.vectorstore.MilvusStore;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/26 23:11
 */
public class MilvusStoreTest {
    private String milvus_apiKey = "271b1b701d3db511d3d03b5910a33a01bdc5d2e9ad24f5e85ecd1c55634ad12b691f4e17b62f2ee0b09d142292676a3291b19a2e";
    private String milvus_endpoint = "https://in03-7b401d24765d2cb.api.gcp-us-west1.zillizcloud.com";
    private String endpoint = "https://api.gptmf.top/";
    private String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

    @Test
    public void embedding_test() {

        //holo数据库知识库向量持久化
        MilvusStore milvusStore = new MilvusStore(milvus_endpoint,milvus_apiKey);
        milvusStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
        milvusStore.init();


        List<String> knowledge = Arrays.asList(
                "百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123",
                "openapi接口传数据是有大小限制在10M以内，这个是nginx限制的要求",
                "lalalal1",
                "lalalal2",
                "lalaala3");
        milvusStore.addTexts(knowledge);

        //知识库向量检索
        RetrievalQA qa = new RetrievalQA();
        qa.setLlm(new OpenAI(endpoint, apiKey)); //chatgpt大模型
        qa.setRetriever(milvusStore.asRetriever());
        qa.setRecommendDocumentCount(2);
        qa.init();

        //qa问答
        String answer = qa.call("OPENAPI有接口大小限制不，有的话是多少");
        System.out.println(answer);
    }
}

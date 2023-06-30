package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.extend.RetrievalChain;
import com.shenyubao.javachain.connection.embeddings.OpenAIEmbeddings;
import com.shenyubao.javachain.connection.loader.Docx2txtLoader;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.vectorstore.MilvusStore;
import com.shenyubao.javachain.connection.vectorstore.MockStore;
import com.shenyubao.javachain.connection.vectorstore.VectorStore;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author shenyubao
 * @date 2023/6/24 12:21
 */
public class EmbeddingTest {
    @Test
    public void embedding_test() {
        String endpoint = "https://api.gptmf.top/";
        String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

        //数据库知识库向量持久化
        MockStore localStore = new MockStore();
        localStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
        //模拟知识库
        List<String> knowledge = Arrays.asList("百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123",
                "openapi接口传数据是有大小限制在10M以内，这个是nginx限制的要求");
        localStore.addTexts(knowledge);

        //知识库向量检索
        RetrievalChain qa = new RetrievalChain();
        qa.setRetriever(localStore.asRetriever());
        qa.setRecommendDocumentCount(10);

        //qa问答
        String answer = qa.call("OPENAPI有接口大小限制不，有的话是多少");
        System.out.println(answer);
    }

    @Test
    public void embedding_loader_test() {
        String endpoint = "https://api.gptmf.top/";
        String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";
        String milvus_apiKey = "271b1b701d3db511d3d03b5910a33a01bdc5d2e9ad24f5e85ecd1c55634ad12b691f4e17b62f2ee0b09d142292676a3291b19a2e";
        String milvus_endpoint = "https://in03-7b401d24765d2cb.api.gcp-us-west1.zillizcloud.com";

        //数据库知识库向量持久化
        VectorStore vectorStore = new MilvusStore(milvus_endpoint, milvus_apiKey);
        vectorStore.init();
        vectorStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings

        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("loader/docx2txt.docx")).getPath();
        Docx2txtLoader loader = new Docx2txtLoader();
        loader.setFilePath(path);
        List<Document> documents = loader.load("10001");
        List<String> documentIds = vectorStore.addDocuments(documents);
        System.out.println("add documents count:" + documentIds.size());

        //知识库向量检索
        RetrievalChain qa = new RetrievalChain();
        qa.setRetriever(vectorStore.asRetriever());
        qa.setRecommendDocumentCount(2);

        //qa问答
        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put(JavaChainConstant.CHAIN_PARAM_DATASET, "10001");
        questionMap.put(JavaChainConstant.CHAIN_PARAM_QUESTION, "百灵AI的浏览器插件是做什么的");

//        Map<String, Object> answer = qa.call(questionMap);
//        System.out.println(answer.get(JavaChainConstant.CHAIN_PARAM_RESULT));

        //清理向量数据库中的文档
        Boolean isRemoved = vectorStore.removeDocuments(documentIds);
        System.out.println(isRemoved);
    }
}

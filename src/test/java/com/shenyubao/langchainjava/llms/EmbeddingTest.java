package com.shenyubao.langchainjava.llms;

import com.shenyubao.langchainjava.chain.retrievalqa.RetrievalQA;
import com.shenyubao.langchainjava.embeddings.OpenAIEmbeddings;
import com.shenyubao.langchainjava.vectorstore.MockStore;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 12:21
 */
public class EmbeddingTest {
    @Test
    public void embedding_test() {
        String endpoint = "https://api.gptmf.top/";
        String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

        //holo数据库知识库向量持久化
        MockStore localStore = new MockStore();
        localStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
        //模拟知识库
        List<String> knowledge = Arrays.asList("百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123",
                "openapi接口传数据是有大小限制在10M以内，这个是nginx限制的要求");
        localStore.addTexts(knowledge);

        //知识库向量检索
        RetrievalQA qa = new RetrievalQA();
        qa.setLlm(new OpenAI(endpoint, apiKey)); //chatgpt大模型
        qa.setRetriever(localStore.asRetriever());
        qa.setRecommendDocumentCount(10);
        qa.init();

        //qa问答
        String answer = qa.call("OPENAPI有接口大小限制不，有的话是多少");
        System.out.println(answer);
    }
}

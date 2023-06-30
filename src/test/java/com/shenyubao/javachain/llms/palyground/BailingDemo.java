package com.shenyubao.javachain.llms.palyground;

import com.shenyubao.javachain.chain.extend.ConversationChain;
import com.shenyubao.javachain.chain.extend.LLMChain;
import com.shenyubao.javachain.chain.extend.StuffDocumentChain;
import com.shenyubao.javachain.chain.extend.RetrievalChain;
import com.shenyubao.javachain.chain.router.SequentialChain;
import com.shenyubao.javachain.connection.embeddings.OpenAIEmbeddings;
import com.shenyubao.javachain.connection.vectorstore.MilvusStore;
import com.shenyubao.javachain.llms.OpenAI;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.PromptConstants;
import com.shenyubao.javachain.prompt.message.AIMessage;
import com.shenyubao.javachain.prompt.message.HumanMessage;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author shenyubao
 * @date 2023/6/30 19:33
 */
public class BailingDemo {


    String endpoint = "https://api.gptmf.top/";
    String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

    private String milvus_apiKey = "271b1b701d3db511d3d03b5910a33a01bdc5d2e9ad24f5e85ecd1c55634ad12b691f4e17b62f2ee0b09d142292676a3291b19a2e";
    private String milvus_endpoint = "https://in03-7b401d24765d2cb.api.gcp-us-west1.zillizcloud.com";

    @Test
    void test_conversation_history() {
        // ConversationChain -> LLMChain
        // 渲染历史对话        -> 请求大语言模型

        //  ConversationChain
        List<BaseMessage> historyMessages = new ArrayList<>();
        historyMessages.add(new HumanMessage("百灵AI都有哪些产品"));
        historyMessages.add(new AIMessage("百灵AI有企业端和用户端，每个端都提供Chat工作台与浏览器插件"));

        ConversationChain conversation = new ConversationChain();
        conversation.setMessages(historyMessages);

        // LLMChain
        OpenAI openAI = new OpenAI(endpoint,apiKey);

        LLMChain llmChain = new LLMChain();
        llmChain.setPrompt(PromptConstants.CONVERSATION_PROMPT_CH);
        llmChain.setLlm(openAI);
        llmChain.setVerbose(true);

        // 构建问题
        SequentialChain sequentialChain = new SequentialChain();
        sequentialChain.setChains(Arrays.asList(conversation, llmChain));
        String response = sequentialChain.call("使用英语描述");
        System.out.println(response);

    }

    @Test
    void test_conversation_knowledge() {
        //  ConversationChain -> RetrievalChain -> StuffDocumentChain -> LLMChain
        // 渲染历史对话 -> 查询知识库       ->  知识库内容渲染       -> 请求大语言模型

        //  ConversationChain
        List<BaseMessage> historyMessages = new ArrayList<>();
        historyMessages.add(new HumanMessage("百灵AI都有哪些产品"));
        historyMessages.add(new AIMessage("百灵AI有企业端和用户端，每个端都提供Chat工作台与浏览器插件"));

        ConversationChain conversation = new ConversationChain();
        conversation.setMessages(historyMessages);

        //  RetrievalChain
        MilvusStore milvusStore = new MilvusStore(milvus_endpoint,milvus_apiKey);
        milvusStore.setEmbedding(new OpenAIEmbeddings(endpoint, apiKey)); //openai提供的embeddings
        milvusStore.init();

        RetrievalChain retrievalChain = new RetrievalChain();
        retrievalChain.setRetriever(milvusStore.asRetriever());
        retrievalChain.setRecommendDocumentCount(2);
        retrievalChain.setDatasetId("10001");

        // StuffDocumentChain
        StuffDocumentChain stuffDocumentChain = new StuffDocumentChain();

        // LLMChain
        OpenAI openAI = new OpenAI(endpoint,apiKey);

        LLMChain llmChain = new LLMChain();
        llmChain.setPrompt(PromptConstants.QA_CONVERSATION_CH);
        llmChain.setLlm(openAI);
        llmChain.setVerbose(true);

        // 构建问题
        SequentialChain sequentialChain = new SequentialChain();
        sequentialChain.setChains(Arrays.asList(conversation, retrievalChain, stuffDocumentChain, llmChain));
        String response = sequentialChain.call("百灵AI的浏览器插件是做什么的");
        System.out.println(response);

    }
}

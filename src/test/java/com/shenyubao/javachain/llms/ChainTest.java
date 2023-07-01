package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.chain.extend.LLMChain;
import com.shenyubao.javachain.llms.sse.OpenAIConsoleStreamListener;
import com.shenyubao.javachain.prompt.template.PromptTemplate;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenyubao
 * @date 2023/6/23 22:33
 */
class ChainTest {
    String endpoint = "https://api.gptmf.top/";
    String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

    @Test
    public void test_chatgpt_run() {
        OpenAI llm = new OpenAI(endpoint, apiKey);

        PromptTemplate prompt = new PromptTemplate();
        prompt.setTemplate("已知信息：\n" +
                "{context}" +
                "\n" +
                "根据上述已知信息，简洁和专业的来回答用户的问题，请选择最匹配的一条信息。如果无法从中得到答案，请说 " +
                "“根据已知信息无法回答该问题” 或 “没有提供足够的相关信息”，不允许在答案中添加编造成分，答案请使用中文。 问题是：\n" +
                "{input}");

        LLMChain chain = new LLMChain();
        chain.setLlm(llm);
        chain.setPrompt(prompt);

        ChainContext chainContext = new ChainContext();
        chainContext.setInput("开放平台如何创建机器人？");
        chainContext.addPromptParam("context","1、百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123 。\n" +
                "2、openapi接口传数据是有大小限制，API调用请求最大报文限制在10M以内，这个是nginx限制的要求。\n");

        ChainContext response = chain.call(chainContext);
        System.out.println(response.getOutput());
    }

    @Test
    public void test_chatgpt_run_stream(){
        OpenAI llm = new OpenAI(endpoint, apiKey);

        PromptTemplate prompt = new PromptTemplate();
        prompt.setTemplate("已知信息：\n" +
                "{context}" +
                "\n" +
                "根据上述已知信息，简洁和专业的来回答用户的问题，请选择最匹配的一条信息。如果无法从中得到答案，请说 " +
                "“根据已知信息无法回答该问题” 或 “没有提供足够的相关信息”，不允许在答案中添加编造成分，答案请使用中文。 问题是：\n" +
                "{input}");

        LLMChain chain = new LLMChain();
        chain.setLlm(llm);
        chain.setIsSteam(true);
        chain.setPrompt(prompt);
        OpenAIConsoleStreamListener eventSourceListener = new OpenAIConsoleStreamListener();
        chain.setEventSourceListener(eventSourceListener);

        ChainContext chainContext = new ChainContext();
        chainContext.setInput("开放平台如何创建机器人？");
        chainContext.addPromptParam("context","1、百灵AI123开放平台创建机器人，可以参考：https://www.bailing.ai/knowledge/123 。\n" +
                "2、openapi接口传数据是有大小限制，API调用请求最大报文限制在10M以内，这个是nginx限制的要求。\n");

        chain.call(chainContext);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
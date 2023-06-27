package com.shenyubao.javachain.llms.llm;

import com.shenyubao.javachain.llms.AzureOpenAI;
import com.shenyubao.javachain.llms.sse.OpenAIConsoleStreamListener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenyubao
 * @date 2023/6/23 22:33
 */
class AzureOpenAITest {
    String endpoint = "https://ai123app.openai.azure.com/";
    String apiKey = "6b10ecc898054dc6a2922a16c034ce4b";
    String modelName = "default";
    @Test
    void test_predict() {
        AzureOpenAI openAI = new AzureOpenAI(endpoint, apiKey, modelName);
        String result = openAI.predict("使用Java写一段代码，获取本机IP地址").outputString();
        System.out.println(result);
    }

    @Test
    void test_predict_stream() {
        AzureOpenAI openAI = new AzureOpenAI(endpoint, apiKey, modelName);
        OpenAIConsoleStreamListener eventSourceListener = new OpenAIConsoleStreamListener();
        openAI.streamPredict("使用Java写一段代码，获取本机IP地址", eventSourceListener);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
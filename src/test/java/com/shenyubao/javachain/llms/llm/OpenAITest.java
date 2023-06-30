package com.shenyubao.javachain.llms.llm;

import com.shenyubao.javachain.llms.OpenAI;
import com.shenyubao.javachain.llms.sse.OpenAIConsoleStreamListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenyubao
 * @date 2023/6/23 22:33
 */
class OpenAITest {
    String endpoint = "https://api.gptmf.top/";
    String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

    @Test
    void test_predict() {
        OpenAI openAI = new OpenAI(endpoint,apiKey);
        String result = openAI.predict("使用Java写一段代码，获取本机IP地址").outputString();
        Assertions.assertTrue(result.length() > 0);
    }

    @Test
    void test_predict_stream() {
        OpenAI openAI = new OpenAI(endpoint,apiKey);
        OpenAIConsoleStreamListener eventSourceListener = new OpenAIConsoleStreamListener();
        openAI.streamPredict("使用Java写一段代码，获取本机IP地址", eventSourceListener);

//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
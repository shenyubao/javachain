package com.shenyubao.javachain.llms.llm;

import com.shenyubao.javachain.llms.OpenAI;
import com.shenyubao.javachain.llms.sse.OpenAIConsoleStreamListener;
import com.shenyubao.javachain.utils.PropertiesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenyubao
 * @date 2023/6/23 22:33
 */
class OpenAITest {
    String endpoint;
    String apiKey;

    @BeforeEach
    void setUp() {
        PropertiesUtils propertiesUtils = new PropertiesUtils("javachain");

        this.endpoint = propertiesUtils.get("openai.endpoint");
        this.apiKey = propertiesUtils.get("openai.apikey");
    }

    @Test
    void test_predict() {
        OpenAI openAI = new OpenAI(endpoint,apiKey);
        String result = openAI.predict("使用Java写一段代码，获取本机IP地址").outputString();
        Assertions.assertTrue(result.length() > 0);
    }

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
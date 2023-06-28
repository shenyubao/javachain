package com.shenyubao.javachain.llms;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.javachain.chain.ConversationChain;
import com.shenyubao.javachain.memory.impl.ConversationBufferMemory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/28 00:20
 */
public class ConversationTest {
    String endpoint = "https://api.gptmf.top/";
    String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";

    @Test
    void test_conversation() {
        OpenAI openAI = new OpenAI(endpoint,apiKey);

        ConversationChain conversation = new ConversationChain(true);
        conversation.setLlm(openAI);
        conversation.setVerbose(true);
        conversation.setMemory(new ConversationBufferMemory());

        Map<String, Object> inputs = new HashMap<>();
        inputs.put("input", "百灵AI是一个可以服务C端和企业端的AI助手");
        Map<String, Object> response = conversation.call(inputs);
        System.out.println(JSON.toJSONString(response));

        inputs = new HashMap<>();
        inputs.put("input", "百灵AI是什么");
        response = conversation.call(inputs);
        System.out.println(JSON.toJSONString(response));
    }
}

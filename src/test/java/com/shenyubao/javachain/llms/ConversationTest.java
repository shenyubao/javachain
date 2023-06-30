package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.chain.extend.ConversationChain;
import com.shenyubao.javachain.memory.impl.ConversationBufferWindowsMemory;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.message.AIMessage;
import com.shenyubao.javachain.prompt.message.HumanMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

//        构建历史消息
        List<BaseMessage> historyMessages = new ArrayList<>();
        historyMessages.add(new HumanMessage("百灵AI都有哪些产品"));
        historyMessages.add(new AIMessage("百灵AI有企业端和用户端，每个端都提供Chat工作台与浏览器插件"));

//        构建包含历史对话的 Lang chain
        ConversationChain conversation = new ConversationChain();
        conversation.setVerbose(true);
        conversation.setMemory(new ConversationBufferWindowsMemory(historyMessages));

//        构建问题
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("input", "使用英语描述");
//        Map<String, Object> response = conversation.call(inputs);
//        System.out.println(JSON.toJSONString(response.get("text")));
    }
}

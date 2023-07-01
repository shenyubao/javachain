package com.shenyubao.javachain.llms.chain;

import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.chain.extend.ConversationChain;
import com.shenyubao.javachain.llms.FakeLLM;
import com.shenyubao.javachain.llms.OpenAI;
import com.shenyubao.javachain.memory.impl.ConversationBufferWindowsMemory;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.message.AIMessage;
import com.shenyubao.javachain.prompt.message.HumanMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/28 00:20
 */
public class ConversationChainTest {

    @Test
    void test_conversation() {
        FakeLLM fakeLLM = new FakeLLM();

//        构建历史消息
        List<BaseMessage> historyMessages = new ArrayList<>();
        historyMessages.add(new HumanMessage("百灵AI都有哪些产品"));
        historyMessages.add(new AIMessage("百灵AI有企业端和用户端，每个端都提供Chat工作台与浏览器插件"));

//        构建包含历史对话的 Lang chain
        ConversationChain conversation = new ConversationChain();
        conversation.setVerbose(true);
        conversation.setMessages(historyMessages);

        ChainContext context = new ChainContext();
        context.setInput("使用英语描述");
        ChainContext result = conversation.call(context);
        Assertions.assertNotNull(result.getPromptParams().get("history"));

    }
}

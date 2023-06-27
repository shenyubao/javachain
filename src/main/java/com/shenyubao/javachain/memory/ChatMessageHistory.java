package com.shenyubao.javachain.memory;

import com.shenyubao.javachain.prompt.message.AIMessage;
import com.shenyubao.javachain.prompt.message.HumanMessage;

/**
 * @author shenyubao
 * @date 2023/6/24 08:06
 */
public class ChatMessageHistory extends BaseChatMessageHistory{
    @Override
    public void addUserMessage(String message) {
        HumanMessage humanMessage = new HumanMessage();
        humanMessage.setContent(message);
        getMessages().add(humanMessage);
    }

    @Override
    public void addAIMessage(String message) {
        AIMessage aiMessage = new AIMessage();
        aiMessage.setContent(message);
        getMessages().add(aiMessage);
    }

    @Override
    public void clear() {
        getMessages().clear();
    }
}

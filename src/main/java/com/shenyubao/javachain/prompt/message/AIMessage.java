package com.shenyubao.javachain.prompt.message;

import com.shenyubao.javachain.prompt.BaseMessage;

/**
 * @author shenyubao
 * @date 2023/6/24 00:29
 */
public class AIMessage extends BaseMessage {
    public AIMessage(String content) {
        super(content);
    }

    public AIMessage() {
    }

    @Override
    public String getType() {
        return "ai";
    }
}

package com.shenyubao.javachain.prompt.message;

import com.shenyubao.javachain.prompt.BaseMessage;

/**
 * @author shenyubao
 * @date 2023/6/24 00:29
 */
public class HumanMessage extends BaseMessage {

    public HumanMessage() {
        super();
    }

    public HumanMessage(String content) {
        super(content);
    }

    @Override
    public String getType() {
        return "human";
    }
}

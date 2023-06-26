package com.shenyubao.langchainjava.prompt.message;

import com.shenyubao.langchainjava.prompt.BaseMessage;

/**
 * @author shenyubao
 * @date 2023/6/24 00:29
 */
public class AIMessage extends BaseMessage {
    @Override
    public String getType() {
        return "ai";
    }
}

package com.shenyubao.javachain.prompt;

import lombok.Data;

import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/23 10:56
 */
@Data
public abstract class BaseMessage {
    private String content;

    private Map<String, Object> addtionalKwargs;

    public abstract String getType();

    public BaseMessage() {
    }

    public BaseMessage(String content) {
        this.content = content;
    }
}

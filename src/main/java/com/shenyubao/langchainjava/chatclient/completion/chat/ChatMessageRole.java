package com.shenyubao.langchainjava.chatclient.completion.chat;

/**
 * @author shenyubao
 * @date 2023/6/23 22:23
 */
public enum ChatMessageRole {
    SYSTEM("system"),
    USER("user"),
    FUNCTION("function"),
    ASSISTANT("assistant");

    ChatMessageRole(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}

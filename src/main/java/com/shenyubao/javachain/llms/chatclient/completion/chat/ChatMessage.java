package com.shenyubao.javachain.llms.chatclient.completion.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shenyubao.javachain.llms.chatclient.model.FunctionCall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author shenyubao
 * @date 2023/6/23 22:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {


    /**
     * 目前支持四个中角色参考官网，进行情景输入：
     * https://platform.openai.com/docs/guides/chat/introduction
     */
    private String role;

    private String content;

    private String name;

    @JsonProperty("function_call")
    private FunctionCall functionCall;

    public static Builder builder() {
        return new Builder();
    }

    private ChatMessage(Builder builder) {
        setRole(builder.role);
        setContent(builder.content);
        setName(builder.name);
        setFunctionCall(builder.functionCall);
    }


    @Getter
    @AllArgsConstructor
    public enum Role {

        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        FUNCTION("function"),
        ;
        private String name;
    }

    public static final class Builder {
        private String role;
        private String content;
        private String name;
        private FunctionCall functionCall;

        public Builder() {
        }

        public Builder role(Role role) {
            this.role = role.getName();
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public ChatMessage build() {
            return new ChatMessage(this);
        }
    }
}

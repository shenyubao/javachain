package com.shenyubao.langchainjava.chatclient.completion.chat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author shenyubao
 * @date 2023/6/23 22:21
 */
@Data
public class ChatCompletionChoice {
    /**
     * This index of this completion in the returned list.
     */
    Integer index;

    /**
     * 请求参数stream为true返回是delta
     */
    @JsonProperty("delta")
    private ChatMessage delta;
    /**
     * 请求参数stream为false返回是message
     */
    @JsonProperty("message")
    private ChatMessage message;

    /**
     * The reason why GPT stopped generating, for example "length".
     */
    @JsonProperty("finish_reason")
    String finishReason;
}

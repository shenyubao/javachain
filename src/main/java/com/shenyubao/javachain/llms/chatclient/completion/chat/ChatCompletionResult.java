package com.shenyubao.javachain.llms.chatclient.completion.chat;

import com.shenyubao.javachain.llms.chatclient.Usage;
import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 22:20
 */
@Data
public class ChatCompletionResult {
    /**
     * Unique id assigned to this chat completion.
     */
    String id;

    /**
     * The type of object returned, should be "chat.completion"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;

    /**
     * The GPT model used.
     */
    String model;

    /**
     * A list of all generated completions.
     */
    List<ChatCompletionChoice> choices;

    /**
     * The API usage for this request.
     */
    Usage usage;
}

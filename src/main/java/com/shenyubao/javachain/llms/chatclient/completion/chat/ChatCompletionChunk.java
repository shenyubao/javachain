package com.shenyubao.javachain.llms.chatclient.completion.chat;

import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 22:48
 */
@Data
public class ChatCompletionChunk {
    /**
     * Unique id assigned to this chat completion.
     */
    String id;

    /**
     * The type of object returned, should be "chat.completion.chunk"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;

    /**
     * The model used.
     */
    String model;

    /**
     * A list of all generated completions.
     */
    List<ChatCompletionChoice> choices;
}

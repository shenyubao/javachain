package com.shenyubao.langchainjava.chatclient.completion;

import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 22:45
 */
@Data
public class CompletionChunk {
    /**
     * A unique id assigned to this completion.
     */
    String id;

    /**
     * https://platform.openai.com/docs/api-reference/create-completion
     * The type of object returned, should be "text_completion"
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
     * A list of generated completions.
     */
    List<CompletionChoice> choices;
}

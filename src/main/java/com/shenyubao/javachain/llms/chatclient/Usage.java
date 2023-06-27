package com.shenyubao.javachain.llms.chatclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author shenyubao
 * @date 2023/6/23 22:15
 */
@Data
public class Usage {
    /**
     * The number of prompt tokens used.
     */
    @JsonProperty("prompt_tokens")
    long promptTokens;

    /**
     * The number of completion tokens used.
     */
    @JsonProperty("completion_tokens")
    long completionTokens;

    /**
     * The number of total tokens used
     */
    @JsonProperty("total_tokens")
    long totalTokens;
}

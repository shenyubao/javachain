package com.shenyubao.javachain.chatclient.embedding;

import com.shenyubao.javachain.chatclient.Usage;
import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 22:29
 */
@Data
public class EmbeddingResult {
    /**
     * The GPTmodel used for generating embeddings
     */
    String model;

    /**
     * The type of object returned, should be "list"
     */
    String object;

    /**
     * A list of the calculated embeddings
     */
    List<Embedding> data;

    /**
     * The API usage for this request
     */
    Usage usage;
}

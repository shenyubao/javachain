package com.shenyubao.javachain.llms.chatclient.embedding;

import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 22:29
 */
@Data
public class Embedding {
    /**
     * The type of object returned, should be "embedding"
     */
    String object;

    /**
     * The embedding vector
     */
    List<Float> embedding;

    /**
     * The position of this embedding in the list
     */
    Integer index;
}

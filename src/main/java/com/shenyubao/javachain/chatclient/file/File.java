package com.shenyubao.javachain.chatclient.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author shenyubao
 * @date 2023/6/23 11:58
 */
@Data
public class File {
    /**
     * The unique id of this file.
     */
    String id;

    /**
     * The type of object returned, should be "file".
     */
    String object;

    /**
     * File size in bytes.
     */
    Long bytes;

    /**
     * The creation time in epoch seconds.
     */
    @JsonProperty("created_at")
    Long createdAt;

    /**
     * The name of the file.
     */
    String filename;

    /**
     * Description of the file's purpose.
     */
    String purpose;
}

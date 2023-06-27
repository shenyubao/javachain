package com.shenyubao.javachain.chatclient;

import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 22:11
 */
@Data
public class ChatClientResponse<T> {
    public List<T> data;
    public String object;
}

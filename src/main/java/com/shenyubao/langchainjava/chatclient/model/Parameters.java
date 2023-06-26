package com.shenyubao.langchainjava.chatclient.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/25 18:52
 */
@Data
@Builder
public class Parameters implements Serializable {
    /**
     * 参数类型
     */
    private String type;
    /**
     * 参数属性、描述
     */
    private Object properties;
    /**
     * 方法必输字段
     */
    private List<String> required;
}

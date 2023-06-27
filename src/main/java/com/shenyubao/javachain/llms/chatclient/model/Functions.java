package com.shenyubao.javachain.llms.chatclient.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shenyubao
 * @date 2023/6/25 18:52
 */
@Data
@Builder
public class Functions implements Serializable {
    /**
     * 方法名称
     */
    private String name;
    /**
     * 方法描述
     */
    private String description;
    /**
     * 方法参数
     * 扩展参数可以继承Parameters自己实现，json格式的数据
     */
    private Parameters parameters;
}

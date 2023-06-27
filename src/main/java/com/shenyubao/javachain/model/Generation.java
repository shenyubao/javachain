package com.shenyubao.javachain.model;

import lombok.Data;

import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/23 07:48
 */
@Data
public class Generation {
    /**
     * 文本输出
     */
    private String text;

    private String role;

    /**
     * 来自LLM的生成信息
     */
    private Map<String, Object> generationInfo;


}

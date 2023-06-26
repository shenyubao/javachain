package com.shenyubao.langchainjava.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LLM返回结果
 *
 * @author shenyubao
 * @date 2023/6/23 07:48
 */
@Data
public class LLMResult {
    private List<Generation> generations;

    private Map<String, Object> llmOutput;

    public String outputString(){
        return generations.stream().map(Generation::getText).collect(Collectors.joining("\n"));
    }
}

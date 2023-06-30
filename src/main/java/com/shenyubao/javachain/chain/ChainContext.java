package com.shenyubao.javachain.chain;

import com.shenyubao.javachain.prompt.template.PromptTemplate;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/30 22:50
 */
public class ChainContext {
    /**
     * 输入内容
     */
    private String input;

    /**
     * 输出内容
     */
    private String output;

    /**
     * Prompt 模板
     */
    private PromptTemplate promptTemplate;

    /**
     * Prompt 渲染参数
     */
    private Map<String, Object> promptParams = new HashMap<>();

    /**
     * 其他补充信息
     */
    private Map<String, Object> additionParams = new HashMap<>();

    public void addPromptParam(String key, String content) {
        promptParams.put(key, content);
    }

    public void addAdditionParam(String key, Object content) {
        additionParams.put(key, content);
    }

    public Map<String, Object> getPromptParams() {
        return promptParams;
    }

    public Map<String, Object> getAdditionParams() {
        return additionParams;
    }

    public void setPromptParams(Map<String, Object> promptParams) {
        this.promptParams = promptParams;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public PromptTemplate getPromptTemplate() {
        return promptTemplate;
    }

    public void setPromptTemplate(PromptTemplate promptTemplate) {
        this.promptTemplate = promptTemplate;
    }
}

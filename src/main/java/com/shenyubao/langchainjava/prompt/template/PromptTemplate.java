package com.shenyubao.langchainjava.prompt.template;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 00:37
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PromptTemplate extends StringPromptTemplate {
    private String template;

    public PromptTemplate() {

    }

    public PromptTemplate(String template) {
        setTemplate(template);
    }

    @Override
    public String getPromptType() {
        return "prompt";
    }

    @Override
    public String format(Map<String, Object> args) {
        if (args == null) {
            return template;
        }
        String realTemplate = template;
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            if (entry.getValue() instanceof String) {
                realTemplate = realTemplate.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
            }
        }
        return realTemplate;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}

package com.shenyubao.langchainjava.prompt.template;

import com.shenyubao.langchainjava.prompt.PromptValue;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 00:25
 */
@Data
public abstract class BasePromptTemplate {
    private List<String> inputVariables;

    /**
     * 创建聊天消息
     *
     * @param args
     * @return
     */
    public abstract PromptValue formatPrompt(Map<String, Object> args);

    public abstract String format(Map<String, Object> args);

    public abstract String getPromptType();
}

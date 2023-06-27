package com.shenyubao.javachain.prompt.template;

import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.prompt.StringPromptValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 00:34
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class StringPromptTemplate extends BasePromptTemplate {
    @Override
    public PromptValue formatPrompt(Map<String, Object> args) {
        StringPromptValue promptValue = new StringPromptValue();
        promptValue.setText(format(args));
        return promptValue;
    }
}

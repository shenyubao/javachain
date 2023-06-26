package com.shenyubao.langchainjava.prompt.template;

import com.shenyubao.langchainjava.prompt.PromptValue;
import com.shenyubao.langchainjava.prompt.StringPromptValue;
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

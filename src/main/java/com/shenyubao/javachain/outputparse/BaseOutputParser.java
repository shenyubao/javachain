package com.shenyubao.javachain.outputparse;

import com.shenyubao.javachain.prompt.PromptValue;

/**
 * @author shenyubao
 * @date 2023/7/2 18:13
 */
public abstract class BaseOutputParser<T> {

    public String getFormatInstructions() {
        return null;
    }

    /**
     * 返回类型键
     *
     * @return
     */
    public String getParserType() {
        return null;
    }


    public abstract T parse(String text);

    public T parseWithPrompt(String completion, PromptValue prompt) {
        return parse(completion);
    }
}

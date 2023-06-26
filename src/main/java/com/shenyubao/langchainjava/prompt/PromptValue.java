package com.shenyubao.langchainjava.prompt;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 10:57
 */
public abstract class PromptValue {
    public abstract List<BaseMessage> toMessages();
}

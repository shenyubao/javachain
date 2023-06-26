package com.shenyubao.langchainjava.prompt.template;

import com.shenyubao.langchainjava.prompt.BaseMessage;
import com.shenyubao.langchainjava.prompt.ChatPromptValue;
import com.shenyubao.langchainjava.prompt.PromptValue;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 00:25
 */
public abstract class BaseChatPromptTemplate extends BasePromptTemplate {
    @Override
    public String format(Map<String, Object> args) {
        return formatPrompt(args).toString();
    }

    @Override
    public PromptValue formatPrompt(Map<String, Object> args) {
        List<BaseMessage> messages = formatMessages(args);
        ChatPromptValue promptValue = new ChatPromptValue();
        promptValue.setMessages(messages);
        return promptValue;
    }

    /**
     * Format args into a list of messages.
     *
     * @param args
     * @return
     */
    public abstract List<BaseMessage> formatMessages(Map<String, Object> args);
}

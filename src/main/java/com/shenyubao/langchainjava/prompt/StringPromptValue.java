package com.shenyubao.langchainjava.prompt;

import com.shenyubao.langchainjava.prompt.message.HumanMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 00:35
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class StringPromptValue extends PromptValue {
    private String text;

    @Override
    public String toString() {
        return text;
    }

    @Override
    public List<BaseMessage> toMessages() {
        HumanMessage humanMessage = new HumanMessage();
        humanMessage.setContent(text);
        return Arrays.asList(new BaseMessage[]{humanMessage});
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

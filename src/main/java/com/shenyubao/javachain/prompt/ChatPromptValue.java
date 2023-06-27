package com.shenyubao.javachain.prompt;

import com.shenyubao.javachain.utils.Methods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 00:26
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ChatPromptValue extends PromptValue{
    private List<BaseMessage> messages;

    @Override
    public List<BaseMessage> toMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return Methods.getBufferString(messages, "Human", "AI");
    }
}

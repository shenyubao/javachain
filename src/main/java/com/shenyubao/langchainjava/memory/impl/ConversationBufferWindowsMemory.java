package com.shenyubao.langchainjava.memory.impl;

import com.shenyubao.langchainjava.memory.BaseChatMemory;
import com.shenyubao.langchainjava.prompt.BaseMessage;
import com.shenyubao.langchainjava.utils.Methods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 08:19
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ConversationBufferWindowsMemory extends ConversationBufferMemory {
    /**
     * 记录存储条数
     */
    private int historyCount = 5;

    @Override
    public Map<String, Object> loadMemoryVariables(Map<String, Object> inputs) {
        List<BaseMessage> windowMessages;
        if (historyCount > 0) {
            List<BaseMessage> messages = (List<BaseMessage>) buffer();
            if (messages.size() / 2 > historyCount) {
                windowMessages = messages.stream().skip(historyCount * 2).collect(Collectors.toList());
            } else {
                windowMessages = messages;
            }
        } else {
            windowMessages = new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        if (isReturnMessages()) {
            map.put(getMemoryKey(), windowMessages);
        } else {
            String bufferStrings = Methods.getBufferString(windowMessages, getHumanPrefix(), getAiPrefix());
            map.put(getMemoryKey(), bufferStrings);
        }
        return map;
    }

    @Override
    public Object buffer() {
        return getChatMemory().getMessages();
    }
}

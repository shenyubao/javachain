package com.shenyubao.javachain.utils;

import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.message.AIMessage;
import com.shenyubao.javachain.prompt.message.ChatMessage;
import com.shenyubao.javachain.prompt.message.HumanMessage;
import com.shenyubao.javachain.prompt.message.SystemMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 00:28
 */
public class Methods {
    public static String getBufferString(List<BaseMessage> messages,
                                         String humanPrefix,
                                         String aiPrefix) {
        List<String> stringMessages = new ArrayList<>();
        messages.stream().forEach(message -> {
            String role = null;
            if (message instanceof HumanMessage) {
                role = humanPrefix;
            } else if (message instanceof AIMessage) {
                role = aiPrefix;
            } else if (message instanceof SystemMessage) {
                role = "System";
            } else if (message instanceof ChatMessage) {
                role = ((ChatMessage) message).getRole();
            } else {
                throw new RuntimeException("Got unsupported message type: " + message.getType());
            }
            stringMessages.add(String.format("%s: %s", role, message.getContent()));
        });
        return stringMessages.stream().collect(Collectors.joining("\n"));
    }

    public static String replacePrompt(String text, Map<String, Object> inputs) {
        for (Map.Entry<String, Object> entry : inputs.entrySet()) {
            text = text.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
        }
        return text;
    }

    /**
     * 计算token长度
     * TODO: 更换计算TOken的二方库
     *
     * @param text
     * @return
     */
    public static Integer tokenCounter(String text) {
        return text.length() / 4;
    }
}

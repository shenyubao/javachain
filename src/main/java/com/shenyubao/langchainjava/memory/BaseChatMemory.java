package com.shenyubao.langchainjava.memory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 08:03
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class BaseChatMemory extends BaseMemory{
    private BaseChatMessageHistory chatMemory = new ChatMessageHistory();

    private String outputKey = "text";

    private String inputKey;

    private boolean returnMessages;

    public void saveContext(Map<String, Object> inputs, Map<String, Object> outputs) {
        String inputStr = getPromptInputKey(inputs);
        String outputStr = null;
        if(outputs.size() == 1) {
            for (Map.Entry<String, Object> entry : outputs.entrySet()) {
                outputStr = (String)entry.getValue();
            }
        } else {
            outputStr = (String) outputs.get(outputKey);
        }
        chatMemory.addUserMessage(inputStr);
        chatMemory.addAIMessage(outputStr);
    }

    public void clear() {
        chatMemory.clear();
    }

    private String getPromptInputKey(Map<String, Object> inputs) {
        String promptInputKey = null;
        if(!StringUtils.isEmpty(inputKey)) {
            promptInputKey = inputs.containsKey(inputKey) ? (String)inputs.get(inputKey) : null;
        } else {
            for (Map.Entry<String, Object> entry : inputs.entrySet()) {
                if (memoryVariables().contains(entry.getKey())) {
                    continue;
                }
                promptInputKey = (String) entry.getValue();
            }
        }
        return promptInputKey;
    }
}

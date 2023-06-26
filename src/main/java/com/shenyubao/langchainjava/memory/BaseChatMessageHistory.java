package com.shenyubao.langchainjava.memory;

import com.shenyubao.langchainjava.prompt.BaseMessage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 08:00
 */
@Data
public abstract class BaseChatMessageHistory {
    private List<BaseMessage> messages = new ArrayList<>();

    /**
     * 向存储添加用户消息
     *
     * @param message
     */
    public abstract void addUserMessage(String message);

    /**
     * 向存储添加机器人消息
     *
     * @param message
     */
    public abstract void addAIMessage(String message);

    /**
     * 清空
     */
    public abstract void clear();
}

package com.shenyubao.javachain.memory.impl;

import com.knuddels.jtokkit.api.EncodingType;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.llms.chatclient.model.ChatCompletion;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.utils.Methods;
import com.shenyubao.javachain.utils.TikTokensUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 08:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConversationBufferWindowsMemory extends ConversationBufferMemory {
    /**
     * 记录存储条数
     */
    private int maxHistoryCount = 3;

    private int maxTokenCount = 1000;

    public ConversationBufferWindowsMemory() {
    }

    @Override
    public Object loadMemoryVariables(ChainContext context) {
        return null;
//        List<BaseMessage> windowMessages;
//        if (maxHistoryCount > 0) {
//            List<BaseMessage> messages = (List<BaseMessage>) buffer();
//            if (messages.size() / 2 > maxHistoryCount) {
//                windowMessages = messages.stream().skip(maxHistoryCount * 2).collect(Collectors.toList());
//            } else {
//                windowMessages = messages;
//            }
//        } else {
//            windowMessages = new ArrayList<>();
//        }
//        while (sumToken(windowMessages) > maxTokenCount) {
//            windowMessages = windowMessages.stream().skip(2).collect(Collectors.toList());
//        }
//
//        return Methods.getBufferString(windowMessages, getHumanPrefix(), getAiPrefix());
    }

    public Integer sumToken(List<BaseMessage> messages) {
        String bufferStrings = Methods.getBufferString(messages, getHumanPrefix(), getAiPrefix());
        //TODO: 将ChatModel 塞入Context
        return TikTokensUtil.tokens(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName(), bufferStrings);
    }



    /**
     * TODO: 历史消息暂时由外部传递进来，后续JavaChain自己维护持久化
     *
     */
    public void initHistoryMessage(List<BaseMessage> messages) {
        getChatMemory().setMessages(messages);
    }

    public ConversationBufferWindowsMemory(List<BaseMessage> messages) {
        super();
        getChatMemory().setMessages(messages);
    }
}

package com.shenyubao.javachain.chain.extend;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.llms.chatclient.model.ChatCompletion;
import com.shenyubao.javachain.memory.impl.ConversationBufferMemory;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.PromptConstants;
import com.shenyubao.javachain.utils.Methods;
import com.shenyubao.javachain.utils.TikTokensUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/28 00:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConversationChain extends Chain {

    private String PROMPT_PARAM_HISTORY = "history";

    private int maxHistoryCount = 3;

//    private int maxTokenCount = 1000;

    private String humanPrefix = "Human";

    private String aiPrefix = "AI";

    private List<BaseMessage> messages;

    public ConversationChain() {
        setMemory(new ConversationBufferMemory());
    }

    @Override
    public ChainContext onCall(ChainContext context) {
        String historyMessage = renderHistoryString(messages);
        context.addPromptParam(PROMPT_PARAM_HISTORY, historyMessage);
        return context;
    }

    public String renderHistoryString(List<BaseMessage> messages) {
        List<BaseMessage> windowMessages;
        if (maxHistoryCount > 0) {
            if (messages.size() / 2 > maxHistoryCount) {
                windowMessages = messages.stream().skip(maxHistoryCount * 2).collect(Collectors.toList());
            } else {
                windowMessages = messages;
            }
        } else {
            windowMessages = new ArrayList<>();
        }
//        while (sumToken(windowMessages) > maxTokenCount) {
//            windowMessages = windowMessages.stream().skip(2).collect(Collectors.toList());
//        }

        return Methods.getBufferString(windowMessages, getHumanPrefix(), getAiPrefix());
    }

    public Integer sumToken(List<BaseMessage> messages) {
        String bufferStrings = Methods.getBufferString(messages, getHumanPrefix(), getAiPrefix());
        //TODO: 将ChatModel 塞入Context
        return TikTokensUtil.tokens(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName(), bufferStrings);
    }

}

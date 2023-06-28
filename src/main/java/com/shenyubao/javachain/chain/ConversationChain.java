package com.shenyubao.javachain.chain;

import com.shenyubao.javachain.memory.impl.ConversationBufferMemory;
import com.shenyubao.javachain.prompt.PromptConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shenyubao
 * @date 2023/6/28 00:18
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ConversationChain extends LLMChain{

    public ConversationChain() {
        setMemory(new ConversationBufferMemory());
        setPrompt(PromptConstants.CONVERSATION_PROMPT_EN);
    }

    public ConversationChain(boolean isCH) {
        setMemory(new ConversationBufferMemory());
        if(isCH) {
            setPrompt(PromptConstants.CONVERSATION_PROMPT_CH);
        }
    }
}

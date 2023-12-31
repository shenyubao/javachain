package com.shenyubao.javachain.memory.impl;

import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.memory.BaseChatMemory;
import com.shenyubao.javachain.utils.Methods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 08:19
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ConversationBufferMemory extends BaseChatMemory {
    private String humanPrefix = "Human";

    private String aiPrefix = "AI";

    private String memoryKey = "history";

    /**
     * 将始终返回内存变量列表
     *
     * @return
     */
    @Override
    public List<String> memoryVariables() {
        return Arrays.asList(new String[]{memoryKey});
    }

    @Override
    public Object loadMemoryVariables(ChainContext context) {
        return Methods.getBufferString(getChatMemory().getMessages(), humanPrefix, aiPrefix);
    }

}

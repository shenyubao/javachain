package com.shenyubao.javachain.chain;

import com.shenyubao.javachain.memory.BaseMemory;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 07:57
 */
@Data
public abstract class Chain {

    /**
     * 内存
     */
    private BaseMemory memory;

    /**
     * 是否打印调试日志
     */
    private boolean verbose;

    /**
     * chain调用 start
     **/

    public ChainContext call(ChainContext context) {
        prepInputs(context);
        ChainContext output = onCall(context);
        prepOutputs(context);
        return output;
    }

    public String call(String question) {
        ChainContext chainContext = new ChainContext();
        chainContext.setInput(question);
        ChainContext output = call(chainContext);
        return output.getOutput();
    }

    public abstract ChainContext onCall(ChainContext inputs);

//    public CompletableFuture<Map<String, Object>> callAsync(Map<String, Object> inputs) {
//        return callAsync(inputs, null);
//    }


    private ChainContext prepInputs(ChainContext context) {
//        if (memory != null) {
//            Map<String, Object> external_context = memory.loadMemoryVariables(context.getPromptParams());
//            context.setPromptParams(external_context);
//        }
        return context;
    }

    private ChainContext prepOutputs(ChainContext context) {
//        if (memory != null) {
//            memory.saveContext(context);
//        }
        return context;
    }
}

package com.shenyubao.langchainjava.chain;

import com.shenyubao.langchainjava.callback.BaseCallbackManager;
import com.shenyubao.langchainjava.memory.BaseMemory;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
     * 是否打印
     */
    private boolean verbose;


//    public CompletableFuture<String> chatAsync(String question) {
//        return CompletableFuture.supplyAsync(() -> chat(question));
//    }

//    public CompletableFuture<Map<String, Object>> runAsync(Map<String, Object> inputs) {
//        return CompletableFuture.supplyAsync(() -> run(inputs));
//    }


    /**
     * chain调用 start
     **/

    public Map<String, Object> call(Map<String, Object> inputs) {
        prepInputs(inputs);
        Map<String, Object> output = onCall(inputs);
        prepOutputs(inputs, output);
        return output;
    }

    public String call(String question) {
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("question", question);
        Map<String, Object> repsonse = call(inputs);
        return (String) repsonse.get("text");
    }

    abstract protected Map<String, Object> onCall(Map<String, Object> inputs);

//    public CompletableFuture<Map<String, Object>> callAsync(Map<String, Object> inputs) {
//        return callAsync(inputs, null);
//    }

    /**
     * chain调用 end
     **/

    public abstract List<String> getInputKeys();

    public abstract List<String> getOutputKeys();

    private Map<String, Object> prepInputs(Map<String, Object> inputs) {
        if (memory != null) {
            Map<String, Object> external_context = memory.loadMemoryVariables(inputs);
            inputs.putAll(external_context);
        }
        return inputs;
    }

    private Map<String, Object> prepOutputs(Map<String, Object> inputs,
                                            Map<String, Object> outputs) {
        if (memory != null) {
            memory.saveContext(inputs, outputs);
        }
        Map<String, Object> map = new HashMap<>();
        map.putAll(inputs);
        map.putAll(outputs);
        return map;
    }
}

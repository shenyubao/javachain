package com.shenyubao.langchainjava.chain;

import com.shenyubao.langchainjava.llms.BaseLLM;
import com.shenyubao.langchainjava.callback.BaseCallbackManager;
import com.shenyubao.langchainjava.model.Generation;
import com.shenyubao.langchainjava.model.LLMResult;
import com.shenyubao.langchainjava.prompt.PromptValue;
import com.shenyubao.langchainjava.prompt.template.BasePromptTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 08:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LLMChain extends Chain {
    /**
     * 提示对象
     */
    private BasePromptTemplate prompt;

    /**
     * 大模型对象
     */
    private BaseLLM llm;

    private String outputKey = "text";

    @Override
    public List<String> getInputKeys() {
        return prompt.getInputVariables();
    }

    @Override
    public List<String> getOutputKeys() {
        return Collections.singletonList(outputKey);
    }

    @Override
    protected Map<String, Object> onCall(Map<String, Object> inputs) {

        try {
            Map<String, Object> outputs = new HashMap<>();
            List<PromptValue> promptValues = Collections.singletonList(inputs).stream()
                    .map(input -> prompt.formatPrompt(input)).collect(Collectors.toList());
            LLMResult llmResult = llm.predict(promptValues);
            if (llmResult.getGenerations().size() > 0) {
                List<Generation> generations = llmResult.getGenerations();
                String text = generations.get(0).getText();
                outputs.put(outputKey, text);
            }
            return outputs;
        } catch (Throwable e) {
            throw e;
        }
    }
}

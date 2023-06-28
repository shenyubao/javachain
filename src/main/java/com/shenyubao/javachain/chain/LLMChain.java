package com.shenyubao.javachain.chain;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.model.Generation;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.prompt.template.BasePromptTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
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


    @Override
    public List<String> getInputKeys() {
        return prompt.getInputVariables();
    }

    @Override
    public List<String> getOutputKeys() {
        return Collections.singletonList(JavaChainConstant.CHAIN_PARAM_RESULT);
    }

    @Override
    protected Map<String, Object> onCall(Map<String, Object> inputs) {

        try {
            List<PromptValue> promptValues = Collections.singletonList(inputs).stream()
                    .map(input -> prompt.formatPrompt(input)).collect(Collectors.toList());
            LLMResult llmResult = llm.predict(promptValues);
            if (llmResult.getGenerations().size() > 0) {
                List<Generation> generations = llmResult.getGenerations();
                String text = generations.get(0).getText();
                inputs.put(JavaChainConstant.CHAIN_PARAM_RESULT, text);
            }
            return inputs;
        } catch (Throwable e) {
            throw e;
        }
    }
}

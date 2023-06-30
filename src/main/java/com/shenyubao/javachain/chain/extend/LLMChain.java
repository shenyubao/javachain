package com.shenyubao.javachain.chain.extend;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.model.Generation;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.prompt.template.BasePromptTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public ChainContext onCall(ChainContext context) {

        try {
            context.addPromptParam("input", context.getInput());

            PromptValue promptValue = prompt.formatPrompt(context.getPromptParams());
            LLMResult llmResult = llm.predict(promptValue);
            if (llmResult.getGenerations().size() > 0) {
                List<Generation> generations = llmResult.getGenerations();
                String text = generations.get(0).getText();
                context.setOutput(text);
            }
            return context;
        } catch (Throwable e) {
            throw e;
        }
    }
}

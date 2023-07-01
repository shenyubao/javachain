package com.shenyubao.javachain.chain.extend;

import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.llms.sse.BaseEventSourceListener;
import com.shenyubao.javachain.model.Generation;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.prompt.template.BasePromptTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;


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

    /**
     * 是否流式返回
     */
    private Boolean isSteam = false;

    /**
     * 流式触发器
     */
    private BaseEventSourceListener eventSourceListener;


    @Override
    public ChainContext onCall(ChainContext context) {
        try {
            context.addPromptParam("input", context.getInput());

            PromptValue promptValue = prompt.formatPrompt(context.getPromptParams());
            if (isSteam) {
                llm.streamPredict(promptValue, eventSourceListener);
            } else {
                LLMResult llmResult = llm.predict(promptValue);
                if (llmResult.getGenerations().size() > 0) {
                    List<Generation> generations = llmResult.getGenerations();
                    String text = generations.get(0).getText();
                    context.setOutput(text);
                }
            }
            return context;
        } catch (Throwable e) {
            throw e;
        }
    }
}

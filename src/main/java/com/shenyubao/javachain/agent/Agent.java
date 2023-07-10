package com.shenyubao.javachain.agent;

import com.shenyubao.javachain.callback.BaseCallbackManager;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.chain.extend.LLMChain;
import com.shenyubao.javachain.outputparse.AgentOutputParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/7/2 18:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Agent extends BaseSingleActionAgent {
    private LLMChain llmChain;
    private AgentOutputParser outputParser;
    private List<String> allowedTools;

    public String stop() {
        return "\n" + observationPrefix().trim();
    }

    public abstract void init(boolean isCH);

    /**
     * 附加观察的前缀
     *
     * @return
     */
    public abstract String observationPrefix();

    /**
     * 附加 LLM 调用的前缀
     *
     * @return
     */
    public abstract String llmPrefix();

    /**
     * 构建让代理继续其思考过程的暂存器
     *
     * @param intermediateSteps
     * @return
     */
    public String constructScratchpad(List<AgentAction> intermediateSteps) {
        String thoughts = "";
        for (AgentAction action : intermediateSteps) {
            thoughts += action.getLog();
            thoughts += "\n" + observationPrefix() + action.getObservation() + "\n" + llmPrefix();
        }
        return thoughts;
    }

    public Object plan(List<AgentAction> intermediateSteps, ChainContext chainContext) {
        fillInputs(intermediateSteps, chainContext);
        ChainContext fullOutput = llmChain.onCall(chainContext);
        return outputParser.parse(fullOutput.getOutput());
    }

    /**
     * 从中间步骤为 LLMChain 创建完整的输入
     *
     * @param intermediateSteps
     * @return
     */
    public ChainContext fillInputs(List<AgentAction> intermediateSteps, ChainContext context) {
        String thoughts = constructScratchpad(intermediateSteps);

        context.addPromptParam("agent_scratchpad", thoughts);
        context.addPromptParam("stop", stop());
        return context;
    }
}

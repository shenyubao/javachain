package com.shenyubao.javachain.callback;

import com.shenyubao.javachain.agent.AgentAction;
import com.shenyubao.javachain.agent.AgentFinish;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.tool.ToolExecuteResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/23 08:09
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CallbackManager extends BaseCallbackManager {
    @Override
    public void onLlmStart(List<PromptValue> serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onLlmStart(serialized));
    }

    @Override
    public void onLlmEnd(LLMResult serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onLlmEnd(serialized));
    }

    @Override
    public void onLlmError(Throwable e) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onLlmError(e));
    }

    @Override
    public void onChainStart(Map<String, Object> serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onChainStart(serialized));
    }

    @Override
    public void onChainEnd(Map<String, Object> serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onChainEnd(serialized));
    }

    @Override
    public void onChainError(Throwable e) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onChainError(e));
    }

    @Override
    public void onToolStart(String serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onToolStart(serialized));

    }

    @Override
    public void onToolEnd(ToolExecuteResult serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onToolEnd(serialized));
    }

    @Override
    public void onToolError(Throwable e) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onToolError(e));
    }

    @Override
    public void onAgentAction(AgentAction serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onAgentAction(serialized));
    }

    @Override
    public void onAgentFinish(AgentFinish serialized) {
        getHandlers().stream().forEach(baseCallbackHandler -> baseCallbackHandler.onAgentFinish(serialized));
    }
}

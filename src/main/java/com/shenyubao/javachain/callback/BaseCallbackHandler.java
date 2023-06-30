package com.shenyubao.javachain.callback;

import com.shenyubao.javachain.agent.AgentAction;
import com.shenyubao.javachain.agent.AgentFinish;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.tool.ToolExecuteResult;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/23 07:45
 */
public interface BaseCallbackHandler {
    void onLlmStart(PromptValue promptValues);

    void onLlmEnd(LLMResult result);

    void onLlmError(Throwable e);

    void onChainStart(Map<String, Object> serialized);

    void onChainEnd(Map<String, Object> serialized);

    void onChainError(Throwable e);

    void onToolStart(String serialized);

    void onToolEnd(ToolExecuteResult serialized);

    void onToolError(Throwable e);

    void onAgentAction(AgentAction serialized);

    void onAgentFinish(AgentFinish serialized);

}

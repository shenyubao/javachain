package com.shenyubao.langchainjava.callback;

import com.shenyubao.langchainjava.agent.AgentAction;
import com.shenyubao.langchainjava.agent.AgentFinish;
import com.shenyubao.langchainjava.chatclient.completion.chat.ChatCompletionResult;
import com.shenyubao.langchainjava.chatclient.model.ChatCompletion;
import com.shenyubao.langchainjava.chatclient.model.Message;
import com.shenyubao.langchainjava.model.LLMResult;
import com.shenyubao.langchainjava.prompt.PromptValue;
import com.shenyubao.langchainjava.tool.ToolExecuteResult;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/23 07:45
 */
public interface BaseCallbackHandler {
    void onLlmStart(List<PromptValue> promptValues);

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

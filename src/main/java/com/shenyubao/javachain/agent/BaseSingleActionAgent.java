package com.shenyubao.javachain.agent;

import com.shenyubao.javachain.callback.BaseCallbackManager;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/7/2 18:00
 */
public abstract class BaseSingleActionAgent {
//    public abstract Object plan(List<AgentAction> )

    public abstract Object plan(List<AgentAction> intermediateSteps, Map<String,Object> inputs, BaseCallbackManager callbackManager);

    public abstract Object plan(List<AgentAction> intermediateSteps, Map<String,Object> inputs);
}

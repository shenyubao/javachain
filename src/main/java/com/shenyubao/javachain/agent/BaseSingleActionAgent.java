package com.shenyubao.javachain.agent;

import com.shenyubao.javachain.callback.BaseCallbackManager;
import com.shenyubao.javachain.chain.ChainContext;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/7/2 18:00
 */
public abstract class BaseSingleActionAgent {
    public abstract Object plan(List<AgentAction> intermediateSteps, ChainContext chainContext);
}

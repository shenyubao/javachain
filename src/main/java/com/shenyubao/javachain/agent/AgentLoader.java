package com.shenyubao.javachain.agent;

import com.shenyubao.javachain.agent.mrkl.ZeroShotAgent;
import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.tool.BaseTool;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/7/2 17:56
 */
public class AgentLoader {
    public static AgentExecutor initializeAgent(List<BaseTool> tools, BaseLLM llm) {
        AgentExecutor agentExecutor = new AgentExecutor();
        ZeroShotAgent agent = new ZeroShotAgent();
        agent.setLlm(llm);
        agent.setTools(tools);
        agent.init(true);
        agentExecutor.setAgent(agent);
        agentExecutor.setTools(tools);
        return agentExecutor;
    }
}

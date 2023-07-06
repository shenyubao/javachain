package com.shenyubao.javachain.agent;

import com.alibaba.druid.util.StringUtils;
import com.shenyubao.javachain.callback.BaseCallbackManager;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.tool.BaseTool;
import com.shenyubao.javachain.tool.ToolExecuteResult;
import lombok.Data;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/7/2 17:57
 */
@Data
public class AgentExecutor extends Chain {
    private BaseSingleActionAgent agent;
    private List<BaseTool> tools;
    private boolean returnIntermediateSteps;
    private Integer maxIterations = 10;
    private Double maxExecutionTime;
    private String earlyStoppingMethod = "force";
    private boolean handleParsingErrors = false;


//    @Override
//    public List<String> getOutputKeys() {
//        if (returnIntermediateSteps) {
//            List<String> values = new ArrayList<>();
//            values.addAll(agent.returnValues());
//            values.add("intermediate_steps");
//            return values;
//        } else {
//            return agent.returnValues();
//        }
//    }

    @Override
    public ChainContext onCall(ChainContext context) {
        try {
            List<AgentAction> intermediateSteps = new ArrayList<>();
            int iterations = 0;
            Double timeElapsed = 0.0d;

            Map<String, BaseTool> nameToToolMap = new TreeMap<>();
            for (BaseTool tool : tools) {
                nameToToolMap.put(tool.getName(), tool);
            }

            while (shouldContinue(iterations, timeElapsed)) {
                Object nextStepOutput = takeNextStep(nameToToolMap, context.getPromptParams(), intermediateSteps, null);
                if (nextStepOutput == null) {
                    return null;
                }
                if (nextStepOutput instanceof AgentFinish) {
                    return returnAgent((AgentFinish) nextStepOutput, intermediateSteps, null);
                }
                intermediateSteps.add((AgentAction) nextStepOutput);
                iterations += 1;
            }

//            if (callbackManager != null) {
//                callbackManager.onChainEnd(inputs);
//            }

            return null;
        } catch (Throwable e) {
//            if (callbackManager != null) {
//                callbackManager.onChainError(e);
//            }
            throw e;
        }
    }


    public BaseTool lookupTool(String name) {
        List<BaseTool> filters = tools.stream().
                filter(tool -> tool.getName().equals(name))
                .collect(Collectors.toList());
        if (!filters.isEmpty()) {
            return filters.get(0);
        }
        return null;
    }

    public boolean shouldContinue(int iterations, Double timeElapsed) {
        if (maxIterations != null && iterations >= maxIterations) {
            return false;
        }
        if (maxExecutionTime != null && timeElapsed >= maxExecutionTime) {
            return false;
        }
        return true;
    }

    private ChainContext returnAgent(AgentFinish output, List<AgentAction> intermediateSteps, BaseCallbackManager callbackManager) {
        if (callbackManager != null) {
            callbackManager.onAgentFinish(output);
        }
        Map<String, Object> finalOutput = output.getReturnValues();
        if (returnIntermediateSteps) {
            finalOutput.put("intermediate_steps", intermediateSteps);
        }
        //TODO:
        return null;
    }

    /**
     * 在思想-行动-观察循环中迈出一步。这可以控制代理如何做出选择并根据选择采取行动
     *
     * @param nameToToolMap
     * @param inputs
     * @param intermediateSteps
     * @return
     */
    public Object takeNextStep(Map<String, BaseTool> nameToToolMap,
                               Map<String, Object> inputs,
                               List<AgentAction> intermediateSteps,
                               BaseCallbackManager callbackManager) {
        Object output = agent.plan(intermediateSteps, inputs, callbackManager);
        if (output == null) {
            return null;
        }
        if (output instanceof AgentFinish) {
            return output;
        }
        List<AgentAction> actions = new ArrayList<>();
        if (output instanceof AgentAction) {
            actions.add((AgentAction) output);
        }
        for (AgentAction agentAction : actions) {
            if (callbackManager != null) {
                callbackManager.onAgentAction(agentAction);
            }
            //可以模糊匹配
            String toolName = containActionName(nameToToolMap, agentAction.getTool());
            if (!StringUtils.isEmpty(toolName)) {
                BaseTool tool = nameToToolMap.get(toolName);
//                boolean returnDirect = tool.isReturnDirect();
                ToolExecuteResult toolExecuteResult = tool.run(agentAction.getToolInput(), callbackManager);
                if (toolExecuteResult == null) {
                    return null;
                }
                if (toolExecuteResult.isInterrupted()) {
                    Map<String, Object> returnValues = new HashMap<>();
                    returnValues.put("output", toolExecuteResult.getOutput());
                    AgentFinish agentFinish = new AgentFinish();
                    agentFinish.setReturnValues(returnValues);
                    agentFinish.setLog(toolExecuteResult.getOutput());
                    return agentFinish;
                }
                agentAction.setObservation(toolExecuteResult.getOutput());
                return agentAction;
            }
        }
        return null;
    }

    /**
     * 模糊匹配
     *
     * @param nameToToolMap
     * @param toolName
     * @return
     */
    private String containActionName(Map<String, BaseTool> nameToToolMap, String toolName) {
        for (Map.Entry<String, BaseTool> nameToToolEntry : nameToToolMap.entrySet()) {
            String key = nameToToolEntry.getKey();
            if (toolName.contains(key)) {
                return key;
            }
        }
        return null;
    }
}

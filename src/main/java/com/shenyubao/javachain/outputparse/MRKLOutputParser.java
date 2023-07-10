package com.shenyubao.javachain.outputparse;

import com.shenyubao.javachain.agent.AgentAction;
import com.shenyubao.javachain.agent.AgentFinish;
import com.shenyubao.javachain.agent.mrkl.MRKLPromptConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shenyubao
 * @date 2023/7/3 00:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MRKLOutputParser extends AgentOutputParser {

    private static final String FINAL_ANSWER_ACTION = "Final Answer:";

    public String getFormatInstructions() {
        return MRKLPromptConstants.FORMAT_INSTRUCTIONS;
    }

    @Override
    public String getParserType() {
        return "mrkl";
    }

    @Override
    public Object parse(String text) {
        if (text.indexOf(FINAL_ANSWER_ACTION) >= 0) {
            Map<String, Object> returnValues = new HashMap<>();
            returnValues.put("output", text.substring(text.indexOf(FINAL_ANSWER_ACTION) + FINAL_ANSWER_ACTION.length()));
            AgentFinish agentFinish = new AgentFinish();
            agentFinish.setReturnValues(returnValues);
            agentFinish.setLog(text);
            return agentFinish;
        }
        String regex = "Action\\s*\\d*\\s*:[\\s]*(.*?)[\\s]*Action\\s*\\d*\\s*Input\\s*\\d*\\s*:[\\s]*(.*)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String action = matcher.group(1);
            String actionInput = matcher.group(2);
            AgentAction agentAction = new AgentAction();
            agentAction.setTool(action);
            agentAction.setToolInput(actionInput);
            agentAction.setLog(text);
            return agentAction;
        }
        throw new RuntimeException(String.format("Could not parse LLM output: %s", text));
    }
}
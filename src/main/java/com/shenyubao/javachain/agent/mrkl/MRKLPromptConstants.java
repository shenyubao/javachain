package com.shenyubao.javachain.agent.mrkl;

/**
 * @author shenyubao
 * @date 2023/7/3 00:03
 */
public class MRKLPromptConstants {
    public static final String PREFIX = "Answer the following questions as best you can. You have access to the following tools:";

    public static final String FORMAT_INSTRUCTIONS = "Use the following format:\n" +
            "\n" +
            "Question: the input question you must answer\n" +
            "Thought: you should always think about what to do\n" +
            "Action: the action to take, should be one of [{tool_names}]\n" +
            "Action Input: the input to the action\n" +
            "Observation: the result of the action\n" +
            "... (this Thought/Action/Action Input/Observation can repeat N times)\n" +
            "Thought: I now know the final answer\n" +
            "Final Answer: the final answer to the original input question";

    public static final String SUFFIX = "Begin!\n" +
            "\n" +
            "Question: {input}\n" +
            "Thought:{agent_scratchpad}";
}

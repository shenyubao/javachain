package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.agent.AgentExecutor;
import com.shenyubao.javachain.agent.AgentLoader;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.tool.BaseTool;
import com.shenyubao.javachain.tool.ToolLoader;
import com.shenyubao.javachain.utils.PropertiesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * TODO:
 *
 * @author shenyubao
 * @date 2023/6/28 22:33
 */
public class AgentTest {
    String endpoint;
    String apiKey;

    @BeforeEach
    void setUp() {
        PropertiesUtils propertiesUtils = new PropertiesUtils("javachain");

        this.endpoint = propertiesUtils.get("openai.endpoint");
        this.apiKey = propertiesUtils.get("openai.apikey");
    }

    @Test
    void testAgent() {
        OpenAI openAI = new OpenAI(endpoint, apiKey);
        List<BaseTool> tools = ToolLoader.loadLools(Arrays.asList("serpapi", "llm-math"), openAI);

        AgentExecutor agentExecutor = AgentLoader.initializeAgent(tools, openAI);
        String question = "张国立的老是谁? 她的年龄的平方数是多少?";

        ChainContext context = new ChainContext();
        context.setInput(question);
        context = agentExecutor.onCall(context);
        System.out.println(context.getOutput());
    }
}

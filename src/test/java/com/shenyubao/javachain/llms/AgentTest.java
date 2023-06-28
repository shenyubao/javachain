package com.shenyubao.javachain.llms;

import org.junit.jupiter.api.Test;

/**
 * @author shenyubao
 * @date 2023/6/28 22:33
 */
public class AgentTest {
    String endpoint = "https://api.gptmf.top/";
    String apiKey = "sk-EqNOl3UM3f0jVKz2C9044f6d3637407eB8D497A636336616";


    @Test
    void test_agent() {
        OpenAI openAI = new OpenAI(endpoint, apiKey);

    }
}

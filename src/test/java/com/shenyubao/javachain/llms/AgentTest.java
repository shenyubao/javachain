package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.utils.PropertiesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TODO:
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
}

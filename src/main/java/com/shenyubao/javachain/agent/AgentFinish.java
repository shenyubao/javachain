package com.shenyubao.javachain.agent;

import lombok.Data;

import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/23 08:01
 */
@Data
public class AgentFinish {
    private Map<String, Object> returnValues;
    private String log;

}

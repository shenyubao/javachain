package com.shenyubao.javachain.agent;

import lombok.Data;

/**
 * @author shenyubao
 * @date 2023/6/23 08:00
 */
@Data
public class AgentAction {

    /**
     * 工具key
     */
    private String tool;

    /**
     * 工具输入
     */
    private String toolInput;

    /**
     * 工具结果
     */
    private String observation;

    /**
     * 思考记录
     */
    private String log;
}

package com.shenyubao.langchainjava.tool;

import lombok.Data;

/**
 * @author shenyubao
 * @date 2023/6/23 08:03
 */
@Data
public class ToolExecuteResult {
    public ToolExecuteResult(String output) {
        setOutput(output);
    }

    public ToolExecuteResult(String output, boolean interrupted) {
        setOutput(output);
        setInterrupted(interrupted);
    }

    /**
     * 工具返回的内容
     */
    private String output;

    /**
     * 是否中断
     */
    private boolean interrupted;

}

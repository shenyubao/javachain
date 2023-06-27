package com.shenyubao.javachain.tool;

import com.shenyubao.javachain.callback.BaseCallbackManager;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author shenyubao
 * @date 2023/6/23 08:02
 */
@Data
public abstract class BaseTool {

    /**
     * 工具名称
     */
    private String name;

    /**
     * 使用用途
     */
    private String description;

    /**
     * 工具参数集合
     */
    private Map<String, Object> args = new TreeMap<>();

    /**
     * 是否直接返回工具的输出，将其设置为 true
     */
    private boolean returnDirect;

    private boolean verbose;

    public ToolExecuteResult run(String toolInput) {
        return run(toolInput, null);
    }

    public abstract ToolExecuteResult run(String toolInput, BaseCallbackManager callbackManager);
}

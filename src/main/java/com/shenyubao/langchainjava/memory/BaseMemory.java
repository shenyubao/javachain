package com.shenyubao.langchainjava.memory;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 07:58
 */
@Data
public abstract class BaseMemory {
    /**
     * 此内存类将动态加载的输入键
     *
     * @return
     */
    public abstract List<String> memoryVariables();

    /**
     * 给定链的文本输入返回键值对
     *
     * @param inputs
     * @return
     */
    public abstract Map<String, Object> loadMemoryVariables(Map<String, Object> inputs);

    /**
     * 将此模型运行的上下文保存到内存中
     *
     * @param inputs
     * @param outputs
     */
    public abstract void saveContext(Map<String, Object> inputs, Map<String, Object> outputs);

    /**
     * 清除内存内容
     */
    public abstract void clear();
}

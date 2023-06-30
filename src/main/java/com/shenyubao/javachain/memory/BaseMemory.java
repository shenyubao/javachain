package com.shenyubao.javachain.memory;

import com.shenyubao.javachain.chain.ChainContext;
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
    public abstract Object loadMemoryVariables(ChainContext context);

    /**
     * 将此模型运行的上下文保存到内存中
     *
     */
    public abstract void saveContext(ChainContext context);

    /**
     * 清除内存内容
     */
    public abstract void clear();
}

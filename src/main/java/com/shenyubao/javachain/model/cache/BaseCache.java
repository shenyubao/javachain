package com.shenyubao.javachain.model.cache;

import com.shenyubao.javachain.model.Generation;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 09:48
 */
public abstract class BaseCache {
    /**
     * Look up based on prompt and llm_string.
     *
     * @param prompt
     * @param llmString
     * @return
     */
    public abstract List<Generation> get(String prompt, String llmString);

    /**
     * Update cache based on prompt and llm_string.
     *
     * @param prompt
     * @param llmString
     * @param returnVal
     */
    public abstract void update(String prompt, String llmString, List<Generation> returnVal);

    /**
     * Clear cache that can take additional keyword arguments.
     */
    public abstract void clear();
}

package com.shenyubao.javachain.config;

import com.shenyubao.javachain.model.cache.BaseCache;
import com.shenyubao.javachain.utils.WorkPropertiesUtils;

/**
 * @author shenyubao
 * @date 2023/6/23 09:47
 */
public class LangEngineConfiguration {
    /**
     * qa recommend num
     */
    public static String RETRIEVAL_QA_RECOMMEND_COUNT = WorkPropertiesUtils.get("retrieval_qa_recommend_count");

    public static BaseCache CurrentCache;
}

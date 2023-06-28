package com.shenyubao.javachain.connection.retriever;

import com.shenyubao.javachain.JavaChainConstant;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 08:26
 */
public abstract class BaseRetriever {
    public static final Integer defaultRecommendCount = 3;

    public List<Document> getRelevantDocuments(String query) {
        return getRelevantDocuments(JavaChainConstant.DEFAULT_DATASET, query, defaultRecommendCount);
    }

    public List<Document> getRelevantDocuments(String query, int recommendCount) {
        return getRelevantDocuments(JavaChainConstant.DEFAULT_DATASET, query, recommendCount);
    }

    public abstract List<Document> getRelevantDocuments(String datasetId, String query, int recommendCount);
}

package com.shenyubao.javachain.connection.retriever;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 08:26
 */
public abstract class BaseRetriever {
    public abstract List<Document> getRelevantDocuments(String query);

    public abstract List<Document> getRelevantDocuments(String query, int recommendCount);
}

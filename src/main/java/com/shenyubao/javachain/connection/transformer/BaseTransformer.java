package com.shenyubao.javachain.connection.transformer;

import com.shenyubao.javachain.connection.retriever.Document;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/28 22:53
 */
public abstract class BaseTransformer {
    /**
     * Transform a list of documents.
     *
     * @param documents
     * @return
     */
    public abstract List<Document> transformDocuments(List<Document> documents);
}

package com.shenyubao.javachain.embeddings;

import com.shenyubao.javachain.chatclient.embedding.Embedding;
import com.shenyubao.javachain.indexes.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shenyubao
 * @date 2023/6/24 12:24
 */
public abstract class Embeddings {
    public List<Document> embedTexts(List<String> texts) {
        List<Document> documents = IntStream.range(0, texts.size()).mapToObj(i -> new Document(i, texts.get(i))).collect(Collectors.toList());
        return embedDocument(documents);
    }

    /**
     * 用到的模型类型
     *
     * @return
     */
    public abstract String getModelType();

    /**
     * 嵌入搜索文档
     *
     * @param documents
     * @return
     */
    public abstract List<Document> embedDocument(List<Document> documents);


    /**
     * 嵌入查询文本
     *
     * @param text
     * @return
     */
    public abstract Embedding embedQuery(String text);
}

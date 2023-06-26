package com.shenyubao.langchainjava.embeddings;

import com.shenyubao.langchainjava.indexes.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 12:24
 */
public abstract class Embeddings {
    public List<Document> embedTexts(List<String> texts) {
        List<Document> documents = texts.stream().map(text -> {
            Document document = new Document();
            document.setPageContent(text);
            return document;
        }).collect(Collectors.toList());
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
     * @param recommend
     * @return
     */
    public abstract List<String> embedQuery(String text, int recommend);
}

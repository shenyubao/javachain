package com.shenyubao.langchainjava.vectorstore;

import com.shenyubao.langchainjava.indexes.BaseRetriever;
import com.shenyubao.langchainjava.indexes.Document;
import com.shenyubao.langchainjava.indexes.VectorStoreRetriever;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 08:29
 */
@Data
public abstract class VectorStore {
    /**
     * 通过嵌入运行更多文档并添加到vectorstore
     *
     * @param texts
     */
    public void addTexts(List<String> texts) {
        List<Document> documents = texts.stream().map(text -> {
            Document document = new Document();
            document.setPageContent(text);
            return document;
        }).collect(Collectors.toList());
        addDocuments(documents);
    }

    /**
     * 通过嵌入运行更多文档并添加到vectorstore
     *
     * @param documents
     */
    public abstract void addDocuments(List<Document> documents);

    /**
     * 返回与查询最相似的文档
     *
     * @param query
     * @param k
     * @return
     */
    public abstract List<Document> similaritySearch(String query, int k);

    public BaseRetriever asRetriever() {
        VectorStoreRetriever retriever = new VectorStoreRetriever();
        retriever.setVectorStore(this);
        return retriever;
    }
}

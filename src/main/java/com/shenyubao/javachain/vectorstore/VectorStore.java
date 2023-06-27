package com.shenyubao.javachain.vectorstore;

import com.shenyubao.javachain.embeddings.Embeddings;
import com.shenyubao.javachain.indexes.BaseRetriever;
import com.shenyubao.javachain.indexes.Document;
import com.shenyubao.javachain.indexes.VectorStoreRetriever;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author shenyubao
 * @date 2023/6/24 08:29
 */
@Data
public abstract class VectorStore {

    private Embeddings embedding;

    /**
     * 通过嵌入运行更多文档并添加到vectorstore
     *
     * @param texts
     */
    public void addTexts(List<String> texts) {
        List<Document> documents = new ArrayList<>();
        for (int i=0; i< texts.size();i++){
            Document document = new Document();
            document.setPageContent(texts.get(i));
            document.setIndex(i);
            document.setUniqueId(UUID.randomUUID().toString());
            document.setEmbedding(embedding.embedQuery(texts.get(i)).getEmbedding());
            documents.add(document);
        }
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

    public abstract void init();
}

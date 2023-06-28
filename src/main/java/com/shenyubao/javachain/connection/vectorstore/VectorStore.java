package com.shenyubao.javachain.connection.vectorstore;

import com.shenyubao.javachain.connection.embeddings.Embeddings;
import com.shenyubao.javachain.connection.retriever.BaseRetriever;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.retriever.VectorStoreRetriever;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        for (int i = 0; i < texts.size(); i++) {
            Document document = new Document();
            document.setPageContent(texts.get(i));
            document.setIndex(i);
            document.setEmbedding(embedding.embedQuery(texts.get(i)).getEmbedding());
            documents.add(document);
        }
        addDocuments(documents);
    }

    /**
     * 通过嵌入运行更多文档并添加到vectorstore
     * String datasetId
     *
     * @param documents
     */
    public abstract List<String> addDocuments(List<Document> documents);

    public abstract Boolean removeDocuments(List<String> documentIds);

    /**
     * 返回与查询最相似的文档
     *
     * @param query
     * @param k
     * @return
     */
    public abstract List<Document> similaritySearch(String datasetId, String query, int k);

    public BaseRetriever asRetriever() {
        VectorStoreRetriever retriever = new VectorStoreRetriever();
        retriever.setVectorStore(this);
        return retriever;
    }

    public abstract void init();
}

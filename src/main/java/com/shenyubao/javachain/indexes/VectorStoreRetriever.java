package com.shenyubao.javachain.indexes;

import com.shenyubao.javachain.vectorstore.VectorStore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 08:27
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VectorStoreRetriever extends BaseRetriever{
    private VectorStore vectorStore;
    private String searchType = "similarity";

    private Integer recommendCount = 3;

    @Override
    public List<Document> getRelevantDocuments(String query) {
        if(searchType.equals("similarity")) {
            return vectorStore.similaritySearch(query, recommendCount);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Document> getRelevantDocuments(String query, int recommendCount) {
        if(searchType.equals("similarity")) {
            return vectorStore.similaritySearch(query, recommendCount);
        }
        return new ArrayList<>();
    }

    /**
     * Add documents to vectorstore.
     *
     * @param documents
     * @return
     */

    public void addDocuments(List<Document> documents) {
        vectorStore.addDocuments(documents);
    }
}

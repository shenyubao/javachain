package com.shenyubao.javachain.connection.retriever;

import com.shenyubao.javachain.connection.vectorstore.VectorStore;
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

    @Override
    public List<Document> getRelevantDocuments(String datasetId, String query, int recommendCount) {
        if(searchType.equals("similarity")) {
            return vectorStore.similaritySearch(datasetId, query, recommendCount);
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

package com.shenyubao.javachain.connection.vectorstore;

import com.shenyubao.javachain.connection.embeddings.Embeddings;
import com.shenyubao.javachain.connection.retriever.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 12:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MockStore extends VectorStore {
    private String collectionName = "javachain";
    private Embeddings embedding;
    private List<Document> collection = new ArrayList<>();

    @Override
    public List<String> addDocuments(List<Document> documents) {
        collection.addAll(embedding.embedDocument(documents));
        return new ArrayList<>();
    }

    @Override
    public void init() {

    }

    @Override
    public Boolean removeDocuments(List<String> documentIds) {
        return null;
    }


    @Override
    public List<Document> similaritySearch(String datasetId, String query, int recommendCount) {
        Collections.shuffle(this.collection, new Random());
        return this.collection.stream()
                .limit(recommendCount)
                .collect(Collectors.toList());
    }
}

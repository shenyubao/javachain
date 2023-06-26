package com.shenyubao.langchainjava.vectorstore;

import com.shenyubao.langchainjava.indexes.Document;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/26 16:45
 */
public class MilvusStore extends VectorStore{
    private String apiKey = "271b1b701d3db511d3d03b5910a33a01bdc5d2e9ad24f5e85ecd1c55634ad12b691f4e17b62f2ee0b09d142292676a3291b19a2e";
    private String endpoint = "https://in03-7b401d24765d2cb.api.gcp-us-west1.zillizcloud.com";

    @Override
    public void addDocuments(List<Document> documents) {

    }

    @Override
    public List<Document> similaritySearch(String query, int k) {
        return null;
    }
}

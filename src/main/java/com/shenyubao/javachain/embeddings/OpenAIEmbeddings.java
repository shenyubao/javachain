package com.shenyubao.javachain.embeddings;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.javachain.chatclient.ChatClient;
import com.shenyubao.javachain.chatclient.embedding.Embedding;
import com.shenyubao.javachain.chatclient.embedding.EmbeddingRequest;
import com.shenyubao.javachain.indexes.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/24 12:26
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class OpenAIEmbeddings extends Embeddings {
    private ChatClient client;

    private String model = "text-embedding-ada-002";
    private static final long DEFAULT_OPENAI_TIMEOUT = 100L;

    public OpenAIEmbeddings(String serverUrl, String apiKey) {
        client = new ChatClient(serverUrl, Duration.ofSeconds(DEFAULT_OPENAI_TIMEOUT), true, apiKey);
    }

    @Override
    public String getModelType() {
        return "1";
    }

    @Override
    public List<Document> embedDocument(List<Document> documents) {
        for (Document document : documents) {
            String questionText = document.getPageContent();
            List<String> messages = new ArrayList<>();
            messages.add(questionText);
            EmbeddingRequest.EmbeddingRequestBuilder builder = EmbeddingRequest.builder()
                    .input(messages)
                    .model(getModel());
            EmbeddingRequest embeddingRequest = builder.build();
            client.createEmbeddings(embeddingRequest).getData().forEach(e -> {
                document.setEmbedding(e.getEmbedding());
                document.setIndex(e.getIndex());
                log.warn("openai embeddings answer:" + JSON.toJSONString(e));
            });
        }
        return documents;
    }

    @Override
    public Embedding embedQuery(String text) {
        EmbeddingRequest.EmbeddingRequestBuilder builder = EmbeddingRequest.builder()
                .input(Collections.singletonList(text))
                .model(getModel());
        EmbeddingRequest embeddingRequest = builder.build();
        List<Embedding> embeddings = client.createEmbeddings(embeddingRequest).getData();
        return embeddings.get(0);
    }

}

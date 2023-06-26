package com.shenyubao.langchainjava.embeddings;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.langchainjava.chatclient.ChatClient;
import com.shenyubao.langchainjava.chatclient.embedding.EmbeddingRequest;
import com.shenyubao.langchainjava.indexes.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        return getLenSafeEmbeddings(documents);
    }

    @Override
    public List<String> embedQuery(String text, int recommend) {
        Document document = new Document();
        document.setPageContent(text);
        return getLenSafeEmbeddings(Collections.singletonList(document)).stream()
                .map(e -> JSON.toJSONString(e.getPageContent()))
                .collect(Collectors.toList());
    }

    private List<Document> getLenSafeEmbeddings(List<Document> documents) {
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
}

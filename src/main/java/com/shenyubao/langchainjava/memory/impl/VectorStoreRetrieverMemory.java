package com.shenyubao.langchainjava.memory.impl;

import com.shenyubao.langchainjava.indexes.Document;
import com.shenyubao.langchainjava.indexes.VectorStoreRetriever;
import com.shenyubao.langchainjava.memory.BaseMemory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 08:23
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VectorStoreRetrieverMemory extends BaseMemory {
    /**
     * VectorStoreRetriever object to connect to.
     */
    private VectorStoreRetriever retriever;

    /**
     * Key name to locate the memories in the result of load_memory_variables.
     */
    private String memoryKey = "history";

    /**
     * Key name to index the inputs to load_memory_variables.
     */
    private String inputKey = "input";

    @Override
    public List<String> memoryVariables() {
        return Arrays.asList(new String[]{memoryKey});
    }

    @Override
    public Map<String, Object> loadMemoryVariables(Map<String, Object> inputs) {
        String query = (String) inputs.get(inputKey);
        List<Document> docs = retriever.getRelevantDocuments(query, retriever.getRecommendCount());
        String result = docs.stream().map(e -> e.getPageContent()).collect(Collectors.joining("\n"));
        Map<String, Object> output = new HashMap<>();
        output.put(memoryKey, result);
        return output;
    }

    @Override
    public void saveContext(Map<String, Object> inputs, Map<String, Object> outputs) {
        List<Document> documents = fromDocuments(inputs, outputs);
        retriever.addDocuments(documents);
    }

    @Override
    public void clear() {

    }

    private List<Document> fromDocuments(Map<String, Object> inputs, Map<String, Object> outputs) {
        List<String> texts = new ArrayList<>();
        for (Map.Entry<String, Object> entry : inputs.entrySet()) {
            if (entry.getKey().equals(memoryKey)) {
                continue;
            }
            texts.add(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, Object> entry : outputs.entrySet()) {
            texts.add(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
        String pageContent = texts.stream().collect(Collectors.joining("\n"));

        List<Document> documents = new ArrayList<>();
        Document document = new Document();
        document.setPageContent(pageContent);
        documents.add(document);
        return documents;
    }
}

package com.shenyubao.javachain.memory.impl;

import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.retriever.VectorStoreRetriever;
import com.shenyubao.javachain.memory.BaseMemory;
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
    public Object loadMemoryVariables(ChainContext context) {
//        List<Document> docs = retriever.getRelevantDocuments(context.getInput());
//        String result = docs.stream().map(Document::getPageContent).collect(Collectors.joining("\n"));
//        Map<String, Object> output = new HashMap<>();
//        output.put(memoryKey, result);
        return context;
    }

    @Override
    public void saveContext(ChainContext context) {
        List<Document> documents = fromDocuments(context);
        retriever.addDocuments(documents);
    }

    @Override
    public void clear() {

    }

    private List<Document> fromDocuments(ChainContext context) {
//        List<String> texts = new ArrayList<>();
//        for (Map.Entry<String, Object> entry : inputs.entrySet()) {
//            if (entry.getKey().equals(memoryKey)) {
//                continue;
//            }
//            texts.add(String.format("%s: %s", entry.getKey(), entry.getValue()));
//        }
//        for (Map.Entry<String, Object> entry : outputs.entrySet()) {
//            texts.add(String.format("%s: %s", entry.getKey(), entry.getValue()));
//        }
//        String pageContent = texts.stream().collect(Collectors.joining("\n"));
//
//        List<Document> documents = new ArrayList<>();
//        Document document = new Document();
//        document.setPageContent(pageContent);
//        documents.add(document);
        return new ArrayList<>();
    }
}

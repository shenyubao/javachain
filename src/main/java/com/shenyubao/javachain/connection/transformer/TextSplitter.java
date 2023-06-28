package com.shenyubao.javachain.connection.transformer;

import com.shenyubao.javachain.connection.retriever.Document;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/28 22:54
 */
@Data
@Slf4j
public abstract class TextSplitter extends BaseTransformer{
    private int chunkSize = 4000;
    private int chunkOverlap = 200;

    @Override
    public List<Document> transformDocuments(List<Document> documents) {
        return splitDocuments(documents);
    }

    /**
     * Split documents.
     *
     * @param documents
     * @return
     */
    public List<Document> splitDocuments(List<Document> documents) {
        List<String> texts = new ArrayList<>();
        List<Map<String, Object>> metadatas = new ArrayList<>();
        for (Document document : documents) {
            texts.add(document.getPageContent());
            metadatas.add(document.getMetadata());
        }
        return createDocuments(texts, metadatas);
    }

    /**
     * Create documents from a list of texts.
     *
     * @param texts
     * @param metadatas
     * @return
     */
    public List<Document> createDocuments(List<String> texts, List<Map<String, Object>> metadatas) {
        List<Document> documents = new ArrayList<>();
        int index = 0;
        for (String text : texts) {
            List<String> chunks = splitText(text);
            for (String chunk : chunks) {
                Document newDoc = new Document();
                newDoc.setPageContent(chunk);
                if(metadatas != null && metadatas.size() == texts.size()) {
                    newDoc.setMetadata(metadatas.get(index));
                }
                documents.add(newDoc);
            }
            index++;
        }
        return documents;
    }

    public List<String> mergeSplits(List<String> splits, String separator) {
        int separator_len = separator.length();
        List<String> docs = new ArrayList<>();
        List<String> current_doc = new ArrayList<>();
        int total = 0;
        for (String d : splits) {
            int _len = d.length();
            if(total + _len + (current_doc.size() > 0 ? separator_len : 0) > chunkSize) {
                if(total > chunkSize) {
                    log.warn("Created a chunk of size {}, which is longer than the specified {}", total, chunkSize);
                }
                if(current_doc.size() > 0) {
                    String doc = join_docs(current_doc, separator);
                    if(doc != null) {
                        docs.add(doc);
                    }
                    while(total > chunkOverlap
                            || (total + _len + (current_doc.size() > 0 ? separator_len : 0) > chunkSize
                            && total > 0)) {
                        total -= current_doc.get(0).length() + (current_doc.size() > 1 ? separator_len : 0);
                        current_doc = current_doc.stream().skip(1).collect(Collectors.toList());
                    }
                }
            }
            current_doc.add(d);
            total += _len + (current_doc.size() > 1 ? separator_len : 0);
        }
        String doc = join_docs(current_doc, separator);
        if(doc != null) {
            docs.add(doc);
        }
        return docs;
    }

    public String join_docs(List<String> docs, String separator) {
        String text = docs.stream().collect(Collectors.joining(separator)).trim();
        if(StringUtils.EMPTY.equals(text)) {
            return null;
        } else {
            return text;
        }
    }

    /**
     * Split text into multiple components.
     *
     * @param text
     * @return
     */
    public abstract List<String> splitText(String text);
}

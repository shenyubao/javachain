package com.shenyubao.javachain.connection.vectorstore.adbpg;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.javachain.connection.embeddings.Embeddings;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.vectorstore.VectorStore;
import com.shenyubao.javachain.connection.vectorstore.adbpg.mapper.AdbPostgresKnowledgeMapper;
import com.shenyubao.javachain.llms.chatclient.embedding.Embedding;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/29 23:12
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class AdbPostgresDB extends VectorStore {
    @Resource
    private Embeddings embedding;

    @Resource
    private AdbPostgresKnowledgeMapper adbPostgresKnowledgeMapper;

    @Override
    public List<String> addDocuments(List<Document> documents) {
        if(documents == null || documents.size() == 0) {
            return null;
        }
        documents = embedding.embedDocument(documents);
        for (Document document : documents) {
            KnowledgeDO knowledgeDO = new KnowledgeDO();
            if(!StringUtils.isEmpty(document.getId())) {
                knowledgeDO.setId(Long.parseLong(document.getId()));
            }
//            knowledgeDO.setType(Integer.valueOf(embedding.getModelType()));
            String embeddingString = JSON.toJSONString(document.getEmbedding())
                    .replaceAll("\\[", "{")
                    .replaceAll("\\]", "}")
                    ;
            knowledgeDO.setEmbedding(document.getEmbedding());
            knowledgeDO.setDatasetId(document.getDatasetID());
            knowledgeDO.setRowContent(document.getPageContent());
            adbPostgresKnowledgeMapper.insert(knowledgeDO);
        }
        return null;
    }

    @Override
    public Boolean removeDocuments(List<String> documentIds) {
        return null;
    }

    @Override
    public List<Document> similaritySearch(String datasetId, String query, int k) {
//        Embedding embedded = embedding.embedQuery(query);
//        embeddingString = embeddingString.replaceAll("\\[", "{")
//                .replaceAll("\\]", "}");
//
//        List<KnowledgeDO> knowledgeDOs = adbPostgresKnowledgeMapper.similaritySearch(embeddingString, Integer.valueOf(embedding.getModelType()), k);

//        return knowledgeDOs.stream().map(e -> {
//            Document document = new Document();
//            document.setId(e.getId().toString());
//            document.setPageContent(filter(e.getRowContent()));
//            return document;
//        }).collect(Collectors.toList());
        return null;
    }

    @Override
    public void init() {

    }

    private String filter(String value) {
        value = value.replaceAll("<[^>]+>", ""); // 去掉所有HTML标签
        value = StringEscapeUtils.unescapeHtml4(value); // 去掉HTML实体
        return value;
    }
}

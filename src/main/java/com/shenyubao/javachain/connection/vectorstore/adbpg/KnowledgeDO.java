package com.shenyubao.javachain.connection.vectorstore.adbpg;

import lombok.Data;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/29 23:11
 */
@Data
public class KnowledgeDO {
    private Long id;
    private String datasetId;
    private List<Float> embedding;
    private String rowContent;
}

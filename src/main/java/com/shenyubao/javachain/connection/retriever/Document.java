package com.shenyubao.javachain.connection.retriever;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 08:25
 */
@Data
public class Document {
    /**
     * 文档的唯一ID
     */
    private String id;

    /**
     * 文档正文内容
     */
    @JSONField(name="page_content")
    private String pageContent;

    /**
     * 文档的元数据
     */
    @JSONField(name="metadata")
    private Map<String, Object> metadata;

    /**
     * 文档的向量化结果
     */
    private List<Float> embedding;

    /**
     * 文档的排序
     */
    private Integer index;

    /**
     * 文档的知识库ID
     */
    private String datasetID;

    public Document() {
    }

    public Document(String id, String pageContent) {
        this.id = id;
        this.pageContent = pageContent;
    }

    public Document(String pageContent) {
        this.pageContent = pageContent;
    }
}

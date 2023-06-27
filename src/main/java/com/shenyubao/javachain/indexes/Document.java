package com.shenyubao.javachain.indexes;

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
    private String uniqueId;

    @JSONField(name="page_content")
    private String pageContent;

    @JSONField(name="metadata")
    private Map<String, Object> metadata;

    private List<Float> embedding;

    private Integer index;

    public Document() {
    }

    public Document(Integer index, String pageContent) {
        this.pageContent = pageContent;
        this.index = index;
    }
}

package com.shenyubao.javachain.connection.embeddings;

/**
 * @author shenyubao
 * @date 2023/6/28 11:17
 */
public enum DocumentType {

    DOCS(1),
    PDF(2),
    UNKNOW(99);

    private final Integer type;

    DocumentType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}

package com.shenyubao.javachain.connection.loader;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.connection.retriever.Document;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/27 13:13
 */
public abstract class BaseLoader {
    /**
     * 将数据加载到文档对象中
     *
     * @return
     */
    public abstract List<Document> load(String datasetID);

    public List<Document> load() {
        return load(JavaChainConstant.DEFAULT_DATASET);
    }
}

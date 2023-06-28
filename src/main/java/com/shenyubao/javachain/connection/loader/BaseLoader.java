package com.shenyubao.javachain.connection.loader;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.transformer.BaseTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/27 13:13
 */
public abstract class BaseLoader {

    private List<BaseTransformer> transformers;

    /**
     * 将数据加载到文档对象中
     *
     * @return
     */
    public abstract List<Document> load(String datasetID);

    public List<Document> onTransformer(List<Document> documents) {
        if (transformers != null) {
            for (BaseTransformer transformer : transformers) {
                documents = transformer.transformDocuments(documents);
            }
        }
        return documents;
    }

    public List<Document> load() {
        return load(JavaChainConstant.DEFAULT_DATASET);
    }

    public void registerTransformer(BaseTransformer transformer) {
        if (transformers == null) {
            transformers = new ArrayList<>();
        }
        transformers.add(transformer);
    }
}

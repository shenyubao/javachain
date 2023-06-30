package com.shenyubao.javachain.chain.extend;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.connection.retriever.BaseRetriever;
import com.shenyubao.javachain.connection.retriever.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * @author shenyubao
 * @date 2023/6/24 12:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RetrievalChain extends Chain {

    /**
     * TopN 知识库推荐数量
     * TODO: 参数化
     */
    private int recommendDocumentCount = 1;
    private BaseRetriever retriever;
    private String datasetId;

    @Override
    public ChainContext onCall(ChainContext context) {
        List<Document> documents = getDocs(datasetId, context.getInput());
        context.addAdditionParam(JavaChainConstant.CHAIN_PARAM_INPUT_DOCUMENTS, documents);
        return context;
    }

    private List<Document> getDocs(String datasetId, String question) {
        return retriever.getRelevantDocuments(datasetId, question, recommendDocumentCount);
    }
}

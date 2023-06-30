package com.shenyubao.javachain.chain.extend;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.connection.retriever.Document;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 12:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StuffDocumentChain extends Chain {
    public static String PROMPT_PARAM_DOCS = "documents";

    private String documentSeparator = "\n\n";

    @Override
    public ChainContext onCall(ChainContext context) {
        try {
            List<Document> documents = (List<Document>) context.getAdditionParams().get(JavaChainConstant.CHAIN_PARAM_INPUT_DOCUMENTS);
            context.addPromptParam(PROMPT_PARAM_DOCS, combineDocs(documents));
            return context;
        } catch (Throwable e) {
            throw e;
        }
    }

    private String combineDocs(List<Document> documents) {
        List<String> docStrings = new ArrayList<>();
        documents.forEach(document -> {
            docStrings.add(document.getPageContent());
        });

        return docStrings.stream().collect(Collectors.joining(documentSeparator));
    }

}

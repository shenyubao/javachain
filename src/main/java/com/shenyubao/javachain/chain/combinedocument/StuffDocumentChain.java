package com.shenyubao.javachain.chain.combinedocument;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.LLMChain;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.prompt.template.BasePromptTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 12:18
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class StuffDocumentChain extends Chain {
    private LLMChain llmChain;

    private BasePromptTemplate documentPrompt;

    private String documentVariableName;

    private String documentSeparator = "\n\n";

    @Override
    public List<String> getInputKeys() {
        return Arrays.asList(JavaChainConstant.CHAIN_PARAM_INPUT_DOCUMENTS);
    }

    @Override
    public List<String> getOutputKeys() {
        return Arrays.asList(JavaChainConstant.CHAIN_PARAM_RESULT_DOCUMENT);
    }

    @Override
    protected Map<String, Object> onCall(Map<String, Object> inputs) {
        try {
            List<Document> documents = (List<Document>) inputs.get(JavaChainConstant.CHAIN_PARAM_INPUT_DOCUMENTS);
            String question = (String) inputs.get("question");
            Map<String, Object> outputs = combineDocs(documents, question);

            return outputs;
        } catch (Throwable e) {
            throw e;
        }
    }

    private Map<String, Object> combineDocs(List<Document> documents, String question) {
        List<String> docStrings = new ArrayList<>();
        documents.stream().forEach(document -> {
            String docString = formatDocument(document);
            docStrings.add(docString);
        });
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(documentVariableName, docStrings.stream().collect(Collectors.joining(documentSeparator)));
        inputs.put("question", question);
        inputs.put("documents", documents);

        return llmChain.call(inputs);
    }


    private String formatDocument(Document document) {
        Map<String, Object> baseInfo = new HashMap<>();
        baseInfo.put("page_content", document.getPageContent());
        return documentPrompt.format(baseInfo);
    }
}

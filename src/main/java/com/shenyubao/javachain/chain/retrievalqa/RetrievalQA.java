package com.shenyubao.javachain.chain.retrievalqa;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.LLMChain;
import com.shenyubao.javachain.chain.combinedocument.StuffDocumentChain;
import com.shenyubao.javachain.connection.retriever.BaseRetriever;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.prompt.PromptConstants;
import com.shenyubao.javachain.prompt.template.PromptTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

/**
 * @author shenyubao
 * @date 2023/6/24 12:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RetrievalQA extends Chain {
    private StuffDocumentChain stuffDocumentChain;
    private BaseLLM llm;
    private PromptTemplate prompt;

    private String inputKey = "query";
    private String outputKey = "result";
    private boolean returnSourceDocuments = false;


    /**
     * TopN 知识库推荐数量
     * TODO: 参数化
     */
    private int recommendDocumentCount = 1;
    private BaseRetriever retriever;

    public void init(boolean isCH) {
        if (prompt == null) {
            prompt = (isCH ? PromptConstants.QA_PROMPT_CH : PromptConstants.QA_PROMPT_EN);
        }
        LLMChain llmChain = new LLMChain();
        llmChain.setLlm(llm);
        llmChain.setPrompt(prompt);

        PromptTemplate documentPrompt = new PromptTemplate();
        documentPrompt.setTemplate("Context:\n{page_content}");

        StuffDocumentChain stuffDocumentsChain = new StuffDocumentChain();
        stuffDocumentsChain.setLlmChain(llmChain);
        stuffDocumentsChain.setDocumentVariableName("context");
        stuffDocumentsChain.setDocumentPrompt(documentPrompt);

        stuffDocumentChain = stuffDocumentsChain;
    }

    public void init() {
        init(false);
    }

    @Override
    public List<String> getInputKeys() {
        return Collections.singletonList(inputKey);
    }

    @Override
    public List<String> getOutputKeys() {
        return Collections.singletonList(outputKey);
    }

    @Override
    protected Map<String, Object> onCall(Map<String, Object> inputs) {
        String question = (String) inputs.get(JavaChainConstant.CHAIN_PARAM_QUESTION);
        String datasetId = (String) inputs.get(JavaChainConstant.CHAIN_PARAM_DATASET);
        List<Document> documents = getDocs(datasetId, question);
        inputs.put(JavaChainConstant.CHAIN_PARAM_INPUT_DOCUMENTS, documents);
        inputs.put(JavaChainConstant.CHAIN_PARAM_QUESTION, question);
        return stuffDocumentChain.call(inputs);
    }

    private List<Document> getDocs(String datasetId, String question) {
        return retriever.getRelevantDocuments(datasetId, question, recommendDocumentCount);
    }
}

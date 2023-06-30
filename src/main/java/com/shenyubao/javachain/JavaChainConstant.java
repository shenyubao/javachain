package com.shenyubao.javachain;

/**
 * @author shenyubao
 * @date 2023/6/28 12:08
 */
public class JavaChainConstant {
    /**
     * 文档的知识库ID
     */
    public static final String DEFAULT_DATASET = "UNKNOW";

//    COMMON
    public static final String CHAIN_PARAM_QUESTION = "question";
    public static final String CHAIN_PARAM_RESULT = "text";

//    RetrievalQA
    public static final String CHAIN_PARAM_DATASET = "datasetId";

//    StuffDocumentChain
    public static final String CHAIN_PARAM_INPUT_DOCUMENTS = "input_documents";
    public static final String CHAIN_PARAM_RESULT_DOCUMENT = "output_text";

//  SimpleSequentialChain
    public static final String CHAIN_PARAM_INPUT = "input";
    public static final String CHAIN_PARAM_OUTPUT= "output";




}

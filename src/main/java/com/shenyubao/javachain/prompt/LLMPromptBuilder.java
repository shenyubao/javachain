package com.shenyubao.javachain.prompt;

/**
 * @author shenyubao
 * @date 2023/6/30 22:05
 */
public class LLMPromptBuilder {

//    public String tpl_Chinese = "请使用以下的上下文来回答最后的问题。如果你不知道答案，就说你不知道，不要试图编造答案。\n" +
//            "当前的上下文:\n {context} \n" +
//            "当前的对话:"
//            "Question: {question}\n" +
//            "Helpful Answer:";

    private String history;
    private String context;
    private String question;
    private String systemInit;

    public String output(){
        return null;
    }
}

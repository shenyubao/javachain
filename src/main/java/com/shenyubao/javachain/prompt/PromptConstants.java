package com.shenyubao.javachain.prompt;

import com.shenyubao.javachain.prompt.template.PromptTemplate;

/**
 * @author shenyubao
 * @date 2023/6/24 12:36
 */
public class PromptConstants {
    public static final String CONVERSATION_DEFAULT_TEMPLATE_EN = "The following is a friendly conversation between a human and an AI. The AI is talkative and provides lots of specific details from its context. If the AI does not know the answer to a question, it truthfully says it does not know.\n" +
            "\n" +
            "Current conversation:\n" +
            "{history}\n" +
            "Human: {input}\n" +
            "AI:";

    public static final String CONVERSATION_DEFAULT_TEMPLATE_CH = "下面是一段人与AI的友好对话。 人工智能很健谈，并根据其上下文提供了许多具体细节。 如果 AI 不知道问题的答案，它会如实说它不知道。\n" +
            "\n" +
            "当前对话:\n" +
            "{history}\n" +
            "Human: {input}\n" +
            "AI:";

    public static final PromptTemplate CONVERSATION_PROMPT_EN = new PromptTemplate(CONVERSATION_DEFAULT_TEMPLATE_EN);
    public static final PromptTemplate CONVERSATION_PROMPT_CH = new PromptTemplate(CONVERSATION_DEFAULT_TEMPLATE_CH);


    /**
     * qa问答采用的模版
     */
    public static final String QA_PROMPT_TEMPLATE_EN = "Use the following pieces of context to answer the question at the end. If you don't know the answer, just say that you don't know, don't try to make up an answer.\n" +
            "\n" +
            "{documents}\n" +
            "\n" +
            "Question: {input}\n" +
            "Helpful Answer:";

    public static final String QA_PROMPT_TEMPLATE_CH = "请使用以下的上下文来回答最后的问题。如果你不知道答案，就说你不知道，不要试图编造答案。\n" +
            "\n" +
            "{documents}\n" +
            "\n" +
            "Question: {input}\n" +
            "Helpful Answer:";

    public static final PromptTemplate QA_PROMPT_EN = new PromptTemplate(QA_PROMPT_TEMPLATE_EN);
    public static final PromptTemplate QA_PROMPT_CH = new PromptTemplate(QA_PROMPT_TEMPLATE_CH);

    public static final String QA_PROMPT_TEMPLATE_CONVERSATION_CH = "请使用以下的上下文来回答最后的问题。如果你不知道答案，就说你不知道，不要试图编造答案。\n" +
            "\n" +
            "{documents}\n" +
            "\n" +
            "Current conversation:\n" +
            "{history}\n" +
            "\n" +
            "Question: {input}\n" +
            "Helpful Answer:";

    public static final PromptTemplate QA_CONVERSATION_CH = new PromptTemplate(QA_PROMPT_TEMPLATE_CONVERSATION_CH);

}

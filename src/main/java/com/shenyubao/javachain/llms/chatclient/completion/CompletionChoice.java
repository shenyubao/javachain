package com.shenyubao.javachain.llms.chatclient.completion;

import lombok.Data;

/**
 * @author shenyubao
 * @date 2023/6/23 22:16
 */
@Data
public class CompletionChoice {

    String text;

    /**
     * This index of this completion in the returned list.
     */
    Integer index;

    /**
     * The log probabilities of the chosen tokens and the top {@link CompletionRequest#logprobs} tokens
     */
    LogProbResult logprobs;

    /**
     * The reason why GPT stopped generating, for example "length".
     */
    String finish_reason;
}

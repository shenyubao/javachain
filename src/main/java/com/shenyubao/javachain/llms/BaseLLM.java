package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.callback.BaseCallbackManager;
import com.shenyubao.javachain.callback.CallbackManager;
import com.shenyubao.javachain.llms.sse.BaseEventSourceListener;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.prompt.StringPromptValue;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseLLM {

    public BaseCallbackManager callbackManager = new CallbackManager();

    public void streamPredict(List<PromptValue> completion, BaseEventSourceListener eventSourceListener){
        eventSourceListener.setBaseCallbackManager(callbackManager);
    }

    protected abstract LLMResult doPredict(List<PromptValue> promptValues);

    /**
     * 大模型语言问答
     *
     * @param promptValues
     * @return
     */
    public LLMResult predict(List<PromptValue> promptValues) {
        callbackManager.onLlmStart(promptValues);
        LLMResult result = null;
        try {
            result = doPredict(promptValues);
        } catch (Throwable e) {
            callbackManager.onLlmError(e);
            throw e;
        } finally {
            callbackManager.onLlmEnd(result);
        }
        return result;
    }

    public LLMResult predict(String question) {
        List<PromptValue> promptValueList = new ArrayList<>();
        StringPromptValue promptValue = new StringPromptValue();
        promptValue.setText(question);
        promptValueList.add(promptValue);
        return predict(promptValueList);
    }

    public void streamPredict(String question, BaseEventSourceListener eventSourceListener){
        List<PromptValue> promptValueList = new ArrayList<>();
        StringPromptValue promptValue = new StringPromptValue();
        promptValue.setText(question);
        promptValueList.add(promptValue);

        streamPredict(promptValueList,eventSourceListener);
    }

}

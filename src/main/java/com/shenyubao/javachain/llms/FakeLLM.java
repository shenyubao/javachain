package com.shenyubao.javachain.llms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shenyubao.javachain.CommonError;
import com.shenyubao.javachain.JavaChainException;
import com.shenyubao.javachain.llms.chatclient.ChatClient;
import com.shenyubao.javachain.llms.chatclient.completion.chat.ChatCompletionChoice;
import com.shenyubao.javachain.llms.chatclient.completion.chat.ChatCompletionRequest;
import com.shenyubao.javachain.llms.chatclient.completion.chat.ChatCompletionResult;
import com.shenyubao.javachain.llms.chatclient.model.ChatCompletion;
import com.shenyubao.javachain.llms.chatclient.model.Message;
import com.shenyubao.javachain.llms.sse.BaseEventSourceListener;
import com.shenyubao.javachain.model.Generation;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.PromptValue;
import com.shenyubao.javachain.utils.WorkPropertiesUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/23 23:52
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class FakeLLM extends BaseLLM {

    @Override
    protected LLMResult doPredict(PromptValue promptValue) {
        log.info("[LLM] LLM Content After Template Render:{}", promptValue);

        LLMResult llmResult = new LLMResult();
        List<Generation> generations = new ArrayList<>();
        Generation generation = new Generation();
        generation.setRole(Message.Role.ASSISTANT.getName());
        generation.setText("我是FakeAI，不知道答案");
        generations.add(generation);


        llmResult.setGenerations(generations);
        return llmResult;
    }
}

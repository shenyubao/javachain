package com.shenyubao.javachain.llms;

import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shenyubao.javachain.CommonError;
import com.shenyubao.javachain.JavaChainException;
import com.shenyubao.javachain.llms.chatclient.ChatClient;
import com.shenyubao.javachain.llms.chatclient.model.ChatCompletion;
import com.shenyubao.javachain.llms.chatclient.model.Message;
import com.shenyubao.javachain.llms.sse.BaseEventSourceListener;
import com.shenyubao.javachain.model.Generation;
import com.shenyubao.javachain.model.LLMResult;
import com.shenyubao.javachain.prompt.BaseMessage;
import com.shenyubao.javachain.prompt.PromptValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @refer https://help.aliyun.com/document_detail/2399481.html
 * @date 2023/6/23 23:52
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class TongYILLM extends BaseLLM {
    private ChatClient chatClient;
    private String apiKey;
    private String modelName = "qwen-plus-v1";

    private static final long DEFAULT_TIMEOUT = 100L;
    private static final int DEFALUT_MAXTOKEN = 1500;

    public TongYILLM(String apiKey) {
        this.apiKey = apiKey;
    }

//    TODO：待支持流式输出
    @Override
    public void streamPredict(PromptValue promptValues, BaseEventSourceListener eventSourceListener) {
        ChatCompletion chatCompletion = converToChatCompletion(promptValues);
        chatCompletion.setStream(true);

        try {
            EventSource.Factory factory = EventSources.createFactory(chatClient.getClient());
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            Request request = new Request.Builder()
                    .url(chatClient.getBaseUrl() + "v1/chat/completions")
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
                    .build();
            //创建事件
            factory.newEventSource(request, eventSourceListener);

            super.streamPredict(promptValues, eventSourceListener);
        } catch (Exception e) {
            throw new JavaChainException(CommonError.PARAM_ERROR, e.getMessage());
        }
    }


    @Override
    protected LLMResult doPredict(PromptValue promptValue) {
        log.info("[LLM] LLM Content After Template Render:{}", promptValue);

        ChatCompletion chatCompletion = converToChatCompletion(promptValue);
        QwenParam qwenParam = convertToRequest(chatCompletion);

        com.alibaba.dashscope.aigc.generation.Generation gen = new com.alibaba.dashscope.aigc.generation.Generation();
        GenerationResult result = null;
        try {
            result = gen.call(qwenParam);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }

        log.info("[LLM] LLM Token Cost, Model:{},totalCost:{}",
                qwenParam.getModel(),
                result.getUsage().getInputTokens() + result.getUsage().getOutputTokens()
        );


        return convertTOLLMResult(result);
    }

    private ChatCompletion converToChatCompletion(PromptValue promptValue) {
        ChatCompletion chatCompletion = new ChatCompletion();
        List<Message> messages = new ArrayList<>();
        for (BaseMessage baseMessage : promptValue.toMessages()) {
            Message message = new Message();
            message.setContent(baseMessage.getContent());
            message.setRole(convertMessageType(baseMessage.getType()));
            messages.add(message);
        }

        chatCompletion.setMessages(messages);
        chatCompletion.setModel(ChatCompletion.Model.QWEN_PLUS_V1.getName());
        return chatCompletion;
    }

    private String convertMessageType(String langChainType) {
        switch (langChainType) {
            case "human":
                return Message.Role.USER.getName();
            case "chat":
            case "ai":
                return Message.Role.ASSISTANT.getName();
            case "system":
                return Message.Role.SYSTEM.getName();
            default:
                throw new JavaChainException(CommonError.NOT_SUPPORT);
        }
    }

    private LLMResult convertTOLLMResult(GenerationResult result) {
        LLMResult llmResult = new LLMResult();
        List<Generation> generations = new ArrayList<>();
        for (GenerationOutput.Choice choice : result.getOutput().getChoices()) {
            Generation generation = new Generation();
            generation.setRole(choice.getMessage().getRole());
            generation.setText(choice.getMessage().getContent());
            generations.add(generation);
        }

        llmResult.setGenerations(generations);
        return llmResult;
    }

    private List<com.alibaba.dashscope.common.Message> convertTOMessages(List<Message> sourceMessages) {
        List<com.alibaba.dashscope.common.Message> messages = new ArrayList<>();
        for (Message message : sourceMessages) {
            com.alibaba.dashscope.common.Message newMessage = new com.alibaba.dashscope.common.Message();
            newMessage.setContent(message.getContent());
            newMessage.setRole(message.getRole());
            messages.add(newMessage);
        }
        return messages;
    }

    private QwenParam convertToRequest(ChatCompletion chatCompletion) {
        QwenParam param =
                QwenParam.builder().model(chatCompletion.getModel())
                        .messages(convertTOMessages(chatCompletion.getMessages()))
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .maxLength(DEFALUT_MAXTOKEN)
                        .topP(chatCompletion.getTopP())
                        .apiKey(this.getApiKey())
                        .enableSearch(false)
                        .build();

        return param;
    }


}

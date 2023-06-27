package com.shenyubao.javachain.llms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shenyubao.javachain.CommonError;
import com.shenyubao.javachain.JavaChainException;
import com.shenyubao.javachain.chatclient.ChatClient;
import com.shenyubao.javachain.chatclient.completion.chat.ChatCompletionChoice;
import com.shenyubao.javachain.chatclient.completion.chat.ChatCompletionRequest;
import com.shenyubao.javachain.chatclient.completion.chat.ChatCompletionResult;
import com.shenyubao.javachain.chatclient.model.ChatCompletion;
import com.shenyubao.javachain.chatclient.model.Message;
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
public class OpenAI extends BaseLLM {
    private ChatClient chatClient;

    private static final String DEFAULT_BASE_URL = "https://api.openai.com/";
    private static final long DEFAULT_OPENAI_TIMEOUT = 100L;

    public OpenAI(String apiKey) {
        String serverUrl = WorkPropertiesUtils.get("OPENAI_SERVER_URL", DEFAULT_BASE_URL);
        chatClient = new ChatClient(serverUrl, Duration.ofSeconds(DEFAULT_OPENAI_TIMEOUT), true, apiKey);
    }

    public OpenAI(String serverUrl, String apiKey) {
        chatClient = new ChatClient(serverUrl, Duration.ofSeconds(DEFAULT_OPENAI_TIMEOUT), true, apiKey);
    }

    @Override
    public void streamPredict(List<PromptValue> promptValues, BaseEventSourceListener eventSourceListener) {
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
    protected LLMResult doPredict(List<PromptValue> promptValues) {
        ChatCompletion chatCompletion = converToChatCompletion(promptValues);
        ChatCompletionRequest chatCompletionRequest = convertToRequest(chatCompletion);

        ChatCompletionResult chatCompletionResult = chatClient.createChatCompletion(chatCompletionRequest);

        log.info("[LLM] LLM Token Cost, Model:{},totalCost:{}",
                chatCompletionResult.getModel(),
                chatCompletionResult.getUsage().getTotalTokens()
        );
//        List<String> answerContentList = new ArrayList<>();
//        chatCompletionResult.getChoices().forEach(e -> {
//            ChatMessage message = e.getMessage();
//            if (message != null) {
//                String role = message.getRole();
//                String answer = message.getContent();
//                log.warn("[LLM] "+ chatCompletion.getModel() + " chat answer:{},{}", role, answer);
//                if (answer != null) {
//                    answerContentList.add(answer);
//                }
//            }
//        });

        return convertTOLLMResult(chatCompletionResult);
    }

    private ChatCompletion converToChatCompletion(List<PromptValue> promptValues) {
        ChatCompletion chatCompletion = new ChatCompletion();
        List<Message> messages = new ArrayList<>();
        for (PromptValue promptValue : promptValues) {
            for (BaseMessage baseMessage : promptValue.toMessages()) {
                Message message = new Message();
                message.setContent(baseMessage.getContent());
                message.setRole(convertMessageType(baseMessage.getType()));
                messages.add(message);
            }
        }
        chatCompletion.setMessages(messages);
        return chatCompletion;
    }

    private String convertMessageType(String langchinType) {
        switch (langchinType) {
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

    private LLMResult convertTOLLMResult(ChatCompletionResult result) {
        LLMResult llmResult = new LLMResult();
        List<Generation> generations = new ArrayList<>();
        for (ChatCompletionChoice choice : result.getChoices()) {
            Generation generation = new Generation();
            generation.setRole(choice.getMessage().getRole());
            generation.setText(choice.getMessage().getContent());
            generations.add(generation);
        }

        llmResult.setGenerations(generations);
        return llmResult;
    }

    private ChatCompletionRequest convertToRequest(ChatCompletion chatCompletion) {
        ChatCompletionRequest.ChatCompletionRequestBuilder builder = ChatCompletionRequest.builder()
                .messages(chatCompletion.getMessages())
                .model(chatCompletion.getModel());
        builder.temperature(chatCompletion.getTemperature());
        builder.maxTokens(chatCompletion.getMaxTokens());
        builder.frequencyPenalty(chatCompletion.getFrequencyPenalty());
        builder.presencePenalty(chatCompletion.getPresencePenalty());
        builder.topP(chatCompletion.getTopP());
        builder.n(chatCompletion.getN());
        builder.stop(chatCompletion.getStop());
        if (chatCompletion.getUser() != null) {
            builder.user(chatCompletion.getUser());
        }
        return builder.build();
    }


}

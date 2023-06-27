package com.shenyubao.javachain.chatclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.shenyubao.javachain.chatclient.completion.chat.ChatCompletionRequest;
import com.shenyubao.javachain.chatclient.completion.chat.ChatCompletionResult;
import com.shenyubao.javachain.chatclient.embedding.EmbeddingRequest;
import com.shenyubao.javachain.chatclient.embedding.EmbeddingResult;
import com.shenyubao.javachain.chatclient.model.Model;
import io.reactivex.Single;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shenyubao
 * @date 2023/6/23 11:15
 */
@Data
public class ChatClient {
    /**
     * 大模型基础地址
     */
    private String baseUrl;

    /**
     * 默认超时时间
     */
    private Duration defaultTimeout;

    private static final ObjectMapper mapper = defaultObjectMapper();

    private ChatClientAPI api;

    private ExecutorService executorService;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private OkHttpClient client;

    /**
     * 授权token
     */
    private String token;

    /**
     * 是否需要授权
     */
    private boolean authentication;

    private String apiVersion = "2023-03-15-preview";

    private String modeSource = "openai";

    public ChatClient() {
        this.client = defaultClient(DEFAULT_TIMEOUT);
    }

    public ChatClient(Duration timeout) {
        this(null, timeout, true, null);
    }

    public ChatClient(String serverUrl, Duration timeout, boolean authentication, String token) {
        this(serverUrl, timeout, authentication, token, "openai");
    }

    public ChatClient(String serverUrl, Duration timeout, boolean authentication, String token, String modeSource) {
        if (serverUrl != null) {
            baseUrl = serverUrl;
        }
        this.modeSource = modeSource;

        setAuthentication(authentication);
        setToken(token);
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(timeout);
        Retrofit retrofit = defaultRetrofit(client, mapper);

        this.api = retrofit.create(ChatClientAPI.class);
        this.executorService = client.dispatcher().executorService();
        this.client = defaultClient(DEFAULT_TIMEOUT);
    }

    public ChatClient(ChatClientAPI api) {
        this.api = api;
        this.executorService = null;
    }

    public ChatClient(ChatClientAPI api, final ExecutorService executorService) {
        this.api = api;
        this.executorService = executorService;
    }

    public List<Model> listModels() {
        return execute(api.listModels()).data;
    }

    public Model getModel(String modelId) {
        return execute(api.getModel(modelId));
    }


    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        return execute(api.createChatCompletion(request));
    }

    public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
        return execute(api.createEmbeddings(request));
    }


    public void shutdownExecutor() {
        Objects.requireNonNull(this.executorService, "executorService must be set in order to shut down");
        this.executorService.shutdown();
    }

    public static <T> T execute(Single<T> apiCall) {
        try {
            return apiCall.blockingGet();
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();
                throw new RuntimeException(errorBody);
            } catch (IOException ex) {
                throw e;
            }
        }
    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    public OkHttpClient defaultClient(Duration timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (authentication) {
            builder.addInterceptor(new AuthenticationInterceptor(token, modeSource));
        }
        return builder.build();
    }

    public Retrofit defaultRetrofit(OkHttpClient client, ObjectMapper mapper) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}

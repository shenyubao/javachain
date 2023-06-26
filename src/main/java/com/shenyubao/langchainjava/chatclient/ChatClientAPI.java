package com.shenyubao.langchainjava.chatclient;

import com.shenyubao.langchainjava.chatclient.completion.chat.ChatCompletionRequest;
import com.shenyubao.langchainjava.chatclient.completion.chat.ChatCompletionResult;
import com.shenyubao.langchainjava.chatclient.embedding.EmbeddingRequest;
import com.shenyubao.langchainjava.chatclient.embedding.EmbeddingResult;
import com.shenyubao.langchainjava.chatclient.model.Model;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * @author shenyubao
 * @date 2023/6/23 11:16
 */
public interface ChatClientAPI {
    @GET("v1/models")
    Single<ChatClientResponse<Model>> listModels();

    @GET("/v1/models/{model_id}")
    Single<Model> getModel(@Path("model_id") String modelId);

    @POST("/v1/chat/completions")
    Single<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request);

    @Streaming
    @POST("/v1/chat/completions")
    Call<ResponseBody> createChatCompletionStream(@Body ChatCompletionRequest request);

    @POST("/v1/embeddings")
    Single<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request);

    @Deprecated
    @POST("/v1/engines/{engine_id}/embeddings")
    Single<EmbeddingResult> createEmbeddings(@Path("engine_id") String engineId, @Body EmbeddingRequest request);

}

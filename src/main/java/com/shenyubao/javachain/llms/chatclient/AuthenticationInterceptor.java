package com.shenyubao.javachain.llms.chatclient;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * @author shenyubao
 * @date 2023/6/23 23:06
 */
public class AuthenticationInterceptor implements Interceptor {

    private final String token;

    private String modeSource;

    public AuthenticationInterceptor(String token, String modeSource) {
        Objects.requireNonNull(token, "chat client token required");
        this.token = token;
        this.modeSource = modeSource;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (modeSource.equals("openai")) {
            builder.header("Authorization", "Bearer " + token);
        } else {
            builder.header("api-key", token);
        }
        Request request = builder.build();
        return chain.proceed(request);
    }
}

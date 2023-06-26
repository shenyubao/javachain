package com.shenyubao.langchainjava.llms.sse;

import com.shenyubao.langchainjava.callback.BaseCallbackManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author shenyubao
 * @date 2023/6/25 17:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public abstract class BaseEventSourceListener extends EventSourceListener {

    private BaseCallbackManager baseCallbackManager;

    protected abstract String getFinishWord();

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        baseCallbackManager.onLlmStart(null);//TODO:
        super.onOpen(eventSource, response);
    }

    @Override
    public void onClosed(EventSource eventSource) {
        baseCallbackManager.onLlmEnd(null);//TODO:
        super.onClosed(eventSource);
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        baseCallbackManager.onLlmError(t);
        super.onFailure(eventSource, t, response);
    }
}

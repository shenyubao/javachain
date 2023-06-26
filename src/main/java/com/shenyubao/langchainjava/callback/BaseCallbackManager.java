package com.shenyubao.langchainjava.callback;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class BaseCallbackManager implements BaseCallbackHandler {
    private List<BaseCallbackHandler> handlers = new ArrayList<>();

    public void addHandler(BaseCallbackHandler handler){
        handlers.add(handler);
    }

    public void removeHandler(BaseCallbackHandler handler){
        handlers.remove(handler);
    }
}

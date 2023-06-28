package com.shenyubao.javachain.connection.transformer;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.javachain.utils.PythonUtils;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/28 23:02
 */
public class TokenTextSplitter extends TextSplitter {
    private String encodingName = "gpt2";

    @Override
    public List<String> splitText(String text) {
        String result = PythonUtils.invokeMethodAsResource(getClass(), "tokenTextSplitter_splitText.py", text);
        List<String> splits = JSON.parseArray(result, String.class);
        return splits;
    }
}

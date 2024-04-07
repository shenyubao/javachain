package com.shenyubao.javachain.llms.llm;



import com.shenyubao.javachain.llms.TongYILLM;
import com.shenyubao.javachain.utils.PropertiesUtils;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TongYiTest {

    String apiKey;

    @BeforeEach
    void setUp() {
        PropertiesUtils propertiesUtils = new PropertiesUtils("javachain");

        this.apiKey = propertiesUtils.get("tongyi.apikey");
    }

    @Test
    void test_predict() {
        TongYILLM tongYILLM = new TongYILLM(apiKey);
        String result = tongYILLM.predict("你是谁").outputString();
        Assertions.assertTrue(result.length() > 0);
    }
}

package com.shenyubao.langchainjava.llms;

import com.shenyubao.langchainjava.prompt.template.FewShotPromptTemplate;
import com.shenyubao.langchainjava.prompt.template.PromptTemplate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/24 00:38
 */
public class PromptTest {
    @Test
    void test_format() {
        String template = "I want you to act as a naming consultant for new companies.\nWhat is a good name for a company that makes {product}?";
        PromptTemplate promptTemplate = new PromptTemplate();
        promptTemplate.setTemplate(template);
        Map<String, Object> context = new HashMap<>();
        context.put("product", "nike");
        String prompt = promptTemplate.format(context);
        System.out.println(prompt);
    }

    @Test
    void test_few_shot() {
        List<Map<String, Object>> examples = new ArrayList<>();
        Map<String, Object> example = new HashMap<>();
        example.put("word", "happy");
        example.put("antonym", "sad");
        examples.add(example);

        example = new HashMap<>();
        example.put("word", "tall");
        example.put("antonym", "short");
        examples.add(example);

        String template = "Word: {word}\nAntonym: {antonym}";

        PromptTemplate promptTemplate = new PromptTemplate();
        promptTemplate.setTemplate(template);

        FewShotPromptTemplate fewShotPromptTemplate = new FewShotPromptTemplate();
        fewShotPromptTemplate.setExamples(examples);
        fewShotPromptTemplate.setExamplePrompt(promptTemplate);
        fewShotPromptTemplate.setPrefix("Give the antonym of every input");
        fewShotPromptTemplate.setSuffix("Word: {input}\nAntonym:");
        fewShotPromptTemplate.setExampleSeparator("\n\n");

        Map<String, Object> context = new HashMap<>();
        context.put("input", "big");
        String prompt = fewShotPromptTemplate.format(context);
        System.out.println(prompt);

    }
}

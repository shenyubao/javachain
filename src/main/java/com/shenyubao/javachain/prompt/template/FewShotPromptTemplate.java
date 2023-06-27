package com.shenyubao.javachain.prompt.template;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/24 00:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FewShotPromptTemplate extends StringPromptTemplate {
    private List<Map<String, Object>> examples;

    private PromptTemplate examplePrompt;

    private String suffix;

    private String prefix;

    private String exampleSeparator = "\n\n";

    @Override
    public String format(Map<String, Object> args) {
        if (examples != null) {
            List<String> exampleStrings = examples.stream()
                    .map(example -> examplePrompt.format(example)).collect(Collectors.toList());

            List<String> pieces = new ArrayList<>();
            pieces.add(prefix);
            pieces.addAll(exampleStrings);
            pieces.add(suffix);

            String prompt = pieces.stream().collect(Collectors.joining(exampleSeparator));

            if (args == null) {
                return prompt;
            }
            String realTemplate = prompt;
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                if (entry.getValue() instanceof String) {
                    realTemplate = realTemplate.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
                }
                return realTemplate;
            }
        }
        return null;
    }

    @Override
    public String getPromptType() {
        return "few_shot";
    }
}

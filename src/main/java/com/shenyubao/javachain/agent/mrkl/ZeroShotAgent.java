package com.shenyubao.javachain.agent.mrkl;

import com.alibaba.fastjson.JSON;
import com.shenyubao.javachain.agent.Agent;
import com.shenyubao.javachain.chain.extend.LLMChain;
import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.outputparse.MRKLOutputParser;
import com.shenyubao.javachain.prompt.template.PromptTemplate;
import com.shenyubao.javachain.tool.BaseTool;
import com.shenyubao.javachain.utils.Methods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MRKL(the Modular Reasoning, Knowledge and Language)
 * @author shenyubao
 * @date 2023/7/2 18:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZeroShotAgent extends Agent {
    private BaseLLM llm;
    private List<BaseTool> tools;
    private String prefix = MRKLPromptConstants.PREFIX;
    private String suffix = MRKLPromptConstants.SUFFIX;

    private String formatInstructions = MRKLPromptConstants.FORMAT_INSTRUCTIONS;

    public void ZeroShotAgent() {
        setOutputParser(new MRKLOutputParser());
    }

    @Override
    public void init(boolean isCH) {
        // TODO isCH模版
        PromptTemplate prompt = createPrompt(tools, prefix, suffix, formatInstructions);
        LLMChain llmChain = new LLMChain();
        llmChain.setLlm(llm);
        llmChain.setPrompt(prompt);
        setLlmChain(llmChain);
        setAllowedTools(tools.stream().map(tool -> tool.getName()).collect(Collectors.toList()));
        if(getOutputParser() == null) {
            setOutputParser(new MRKLOutputParser());
        }
    }

    /**
     * Prefix to append the observation with.
     *
     * @return
     */
    @Override
    public String observationPrefix() {
        return "Observation: ";
    }

    /**
     * Prefix to append the llm call with.
     *
     * @return
     */
    @Override
    public String llmPrefix() {
        return "Thought:";
    }


    /**
     * 以零镜头代理的风格创建提示
     *
     * @param tools
     * @param prefix
     * @param suffix
     * @param formatInstructions
     * @return
     */
    public PromptTemplate createPrompt(List<BaseTool> tools,
                                       String prefix,
                                       String suffix,
                                       String formatInstructions) {
        if(prefix == null) {
            prefix = MRKLPromptConstants.PREFIX;
        }
        if(suffix == null) {
            suffix = MRKLPromptConstants.SUFFIX;
        }
        if(formatInstructions == null) {
            formatInstructions = MRKLPromptConstants.FORMAT_INSTRUCTIONS;
        }

        List<String> toolStringList = new ArrayList<>();
        List<String> toolNameList = new ArrayList<>();
        for (BaseTool tool : tools) {
            toolStringList.add(String.format("%s: %s args: %s", tool.getName(), tool.getDescription(), JSON.toJSONString(tool.getArgs())));
            toolNameList.add(tool.getName());
        }
        String toolStrings = toolStringList.stream().collect(Collectors.joining("\n"));
        String toolNames = toolNameList.stream().collect(Collectors.joining(", "));
        Map<String, Object> toolNameMap = new HashMap<>();
        toolNameMap.put("tool_names", toolNames);
        formatInstructions = Methods.replacePrompt(formatInstructions, toolNameMap);

        List<String> templateStringList = new ArrayList<>();
        templateStringList.add(prefix);
        templateStringList.add(toolStrings);
        templateStringList.add(formatInstructions);
        templateStringList.add(suffix);
        String template = templateStringList.stream().collect(Collectors.joining("\n\n"));
        PromptTemplate promptTemplate = new PromptTemplate();
        promptTemplate.setTemplate(template);
        return promptTemplate;
    }
}

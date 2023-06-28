package com.shenyubao.javachain.tool;

import com.shenyubao.javachain.llms.BaseLLM;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/28 22:38
 */
public class ToolLoader {
    public static List<BaseTool> loadLools(List<String> toolNames, BaseLLM llm) {
        List<BaseTool> baseTools = new ArrayList<>();
        for (String toolName : toolNames) {
            if(toolName.equals("llm-math")) {
                LLMMathTool tool = new LLMMathTool();
                tool.setLlm(llm);
                baseTools.add(tool);
            } else if(toolName.equals("serpapi")) {
                SearchAPITool tool = new SearchAPITool();
                baseTools.add(tool);
            }
        }
        return baseTools;
    }

}

package com.shenyubao.javachain.tool.serpapi;

import com.shenyubao.javachain.tool.BaseTool;
import com.shenyubao.javachain.tool.ToolExecuteResult;

/**
 * @author shenyubao
 * @date 2023/6/28 22:47
 */
public class SERPAPITool extends BaseTool {
    public SERPAPITool() {
        setName("serpapi");
        setDescription("A search engine. Useful for when you need to answer questions about current events. Input should be a search query.");
    }

    @Override
    public ToolExecuteResult run(String toolInput) {
        if(toolInput.equals("\"Camila Morrone age\"")) {
            return new ToolExecuteResult("25 years");
        }
        return new ToolExecuteResult("Camila Morrone");
    }
}

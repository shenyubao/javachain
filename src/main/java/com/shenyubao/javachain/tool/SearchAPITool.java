package com.shenyubao.javachain.tool;

/**
 * @author shenyubao
 * @date 2023/6/28 22:47
 */
public class SearchAPITool extends BaseTool{
    public SearchAPITool() {
        setName("search");
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

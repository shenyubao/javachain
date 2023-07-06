package com.shenyubao.javachain.outputparse;

/**
 * @author shenyubao
 * @date 2023/7/2 18:15
 */
public abstract class AgentOutputParser extends BaseOutputParser<Object>{
    public abstract Object parse(String text);
}

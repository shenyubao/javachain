package com.shenyubao.langchainjava.callback;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.langchainjava.agent.AgentAction;
import com.shenyubao.langchainjava.agent.AgentFinish;
import com.shenyubao.langchainjava.model.LLMResult;
import com.shenyubao.langchainjava.prompt.PromptValue;
import com.shenyubao.langchainjava.tool.ToolExecuteResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author shenyubao
 * @date 2023/6/23 08:19
 */
@Data
@Slf4j
public class StdOutCallbackHandler implements BaseCallbackHandler {
    static ThreadLocal<String> _context = new ThreadLocal<>();

    /**
     * 设置上下文
     */
    public static void setContext(String context) {
        if (context == null) {
            return;
        }

        _context.set(context);
    }

    /**
     * 获取上下文
     */
    public static String getContext() {
        return _context.get();
    }

    /**
     * 移除上下文
     */
    public static void removeContext(boolean isDeepClear) {
        String c = _context.get();

        try {
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            _context.set(null);
            _context.remove();
        }
    }

    @Override
    public void onLlmStart(List<PromptValue> serialized) {
        String traceId = getContext();
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        setContext(traceId);
        log.info("onLlmStart traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));
    }

    @Override
    public void onLlmEnd(LLMResult serialized) {
        String traceId = getContext();
        log.info("onLlmEnd traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));
    }

    @Override
    public void onLlmError(Throwable e) {
        String traceId = getContext();
        log.info("onLlmError traceId:{}, e:{}", traceId, e);
    }

    @Override
    public void onChainStart(Map<String, Object> serialized) {
        String traceId = getContext();
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        setContext(traceId);
        log.info("onChainStart traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));
    }

    @Override
    public void onChainEnd(Map<String, Object> serialized) {
        String traceId = getContext();
        log.info("onChainEnd traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));
    }

    @Override
    public void onChainError(Throwable e) {
        String traceId = getContext();
        log.info("onChainError traceId:{}, e:{}", traceId, e);
    }

    @Override
    public void onToolStart(String serialized) {
        String traceId = getContext();
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        setContext(traceId);
        log.info("onToolStart traceId:{}, serialized:{}", traceId, serialized);

    }

    @Override
    public void onToolEnd(ToolExecuteResult serialized) {
        String traceId = getContext();
        log.info("onToolEnd traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));

    }

    @Override
    public void onToolError(Throwable e) {
        String traceId = getContext();
        log.info("onToolError traceId:{}, serialized:{}", traceId, e);
    }

    @Override
    public void onAgentAction(AgentAction serialized) {
        String traceId = getContext();
        log.info("onAgentAction traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));
    }

    @Override
    public void onAgentFinish(AgentFinish serialized) {
        String traceId = getContext();
        log.info("onAgentFinish traceId:{}, serialized:{}", traceId, JSON.toJSONString(serialized));
    }

}

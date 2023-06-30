package com.shenyubao.javachain.chain.extend;

import com.shenyubao.javachain.chain.ChainContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenyubao
 * @date 2023/6/30 19:34
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class FakeChain extends LLMChain {

    private String id;

    public FakeChain(String id) {
        this.id = id;
    }

    @Override
    public ChainContext onCall(ChainContext context) {
        log.info("[CHAIN] FakeChain running in: {}, value: {}", id, context.getAdditionParams().get("test"));
        Integer value = (Integer) context.getAdditionParams().get("test");
        value = value + 1;
        context.addAdditionParam("test", value);
        return context;
    }
}

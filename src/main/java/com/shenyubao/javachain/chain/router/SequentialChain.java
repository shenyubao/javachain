package com.shenyubao.javachain.chain.router;

import com.shenyubao.javachain.JavaChainConstant;
import com.shenyubao.javachain.chain.Chain;
import com.shenyubao.javachain.chain.ChainContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author shenyubao
 * @date 2023/6/30 19:27
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class SequentialChain extends Chain {

    private List<Chain> chains;

    public SequentialChain() {
        chains = new ArrayList<>();
    }

    @Override
    public ChainContext onCall(ChainContext context) {
        for (Chain chain : chains) {
            log.info("[CHAIN] begin to run chain:{}", chain.getClass());
            context = chain.onCall(context);
            log.info("[CHAIN] finish to run chain:{}", chain.getClass());
        }
        return context;
    }
}

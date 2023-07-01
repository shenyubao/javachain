package com.shenyubao.javachain.llms.chain;

import com.shenyubao.javachain.chain.ChainContext;
import com.shenyubao.javachain.chain.extend.FakeChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author shenyubao
 * @date 2023/6/30 23:45
 */
public class SequentialChainTest {
    @Test
    void testSequentialChain() {
        FakeChain fakeChain1 = new FakeChain("fakeChain1");
        FakeChain fakeChain2 = new FakeChain("fakeChain2");
        com.shenyubao.javachain.chain.router.SequentialChain sequentialChain = new com.shenyubao.javachain.chain.router.SequentialChain();
        sequentialChain.setChains(Arrays.asList(fakeChain1,fakeChain2));
        ChainContext context = new ChainContext();
        context.addAdditionParam("test", 0);

        ChainContext response = sequentialChain.call(context);
        Assertions.assertEquals(response.getAdditionParams().get("test"),2);
    }
}

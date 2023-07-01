package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.connection.loader.PdfLoader;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.transformer.RecursiveCharacterTextSplitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * @author shenyubao
 * @date 2023/6/28 23:14
 */
public class TransformerTest {
    @Test
    void testTextSplit() {
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("loader/layout-parser-paper.pdf")).getPath();
        PdfLoader loader = new PdfLoader();
        loader.setFilePath(path);
        loader.registerTransformer(new RecursiveCharacterTextSplitter());
        List<Document> documents = loader.load("10001");
        Assertions.assertTrue(documents.size()>0);
    }
}

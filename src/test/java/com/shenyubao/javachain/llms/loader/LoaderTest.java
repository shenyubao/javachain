package com.shenyubao.javachain.llms.loader;

import com.shenyubao.javachain.connection.loader.Docx2txtLoader;
import com.shenyubao.javachain.connection.retriever.Document;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * @author shenyubao
 * @date 2023/6/27 23:39
 */
public class LoaderTest {

    @Test
    public void docx2txt() {
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("loader/docx2txt.docx")).getPath();
        Docx2txtLoader loader = new Docx2txtLoader();
        loader.setFilePath(path);
        List<Document> documents = loader.load();
        System.out.println(documents);
    }
}

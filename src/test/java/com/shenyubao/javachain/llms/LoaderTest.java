package com.shenyubao.javachain.llms;

import com.shenyubao.javachain.connection.loader.Docx2txtLoader;
import com.shenyubao.javachain.connection.loader.GitLoader;
import com.shenyubao.javachain.connection.loader.PdfLoader;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.connection.transformer.RecursiveCharacterTextSplitter;
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

    @Test
    public void loadPdf(){
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("loader/layout-parser-paper.pdf")).getPath();
        PdfLoader loader = new PdfLoader();
        loader.setFilePath(path);
        loader.registerTransformer(new RecursiveCharacterTextSplitter());
        List<Document> documents = loader.load("10001");
        System.out.println(documents);
    }

    @Test
    public void loadGit(){
        GitLoader loader = new GitLoader();
        loader.setRepoPath("/Users/shenyubao/Documents/Projects/research/langchain");
        loader.setBranch("master");
        List<Document> documents = loader.load("10001");
        System.out.println(documents);
    }
}

package com.shenyubao.javachain.connection.loader;

import com.alibaba.fastjson2.JSON;
import com.shenyubao.javachain.connection.retriever.Document;
import com.shenyubao.javachain.utils.PythonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/30 18:53
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class BSHTMLLoader extends BaseLoader {

    private String filePath;

    @Override
    public List<Document> load(String dataSetId) {
        String result = PythonUtils.invokeMethodAsResource(getClass(), "bsHtmlLoader.py", filePath);
        List<Document> documentList = JSON.parseArray(result, Document.class);

        for (Document document: documentList){
            document.setDatasetID(dataSetId);
        }
        documentList = onTransformer(documentList);
        return documentList;
    }
}

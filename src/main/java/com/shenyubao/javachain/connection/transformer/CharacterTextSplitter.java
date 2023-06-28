package com.shenyubao.javachain.connection.transformer;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/28 22:54
 */
public class CharacterTextSplitter extends TextSplitter {
    private String separator = "\n\n";

    /**
     * Split incoming text and return chunks.
     *
     * @param text
     * @return
     */
    @Override
    public List<String> splitText(String text) {
        List<String> splits;
        if (!StringUtils.isEmpty(separator)) {
            splits = Arrays.asList(text.split(separator));
        } else {
            splits = Arrays.asList(new String[]{text});
        }
        return mergeSplits(splits, separator);
    }
}

package com.shenyubao.javachain.connection.transformer;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/28 22:59
 */
public class RecursiveCharacterTextSplitter extends TextSplitter{
    private List<String> separators = Arrays.asList(new String[] { "\n\n", "\n", " ", "" });

    /**
     * Split incoming text and return chunks.
     *
     * @param text
     * @return
     */
    @Override
    public List<String> splitText(String text) {
        List<String> final_chunks = new ArrayList<>();
        String separator = separators.get(separators.size() - 1);
        for (String s : separators) {
            if(StringUtils.EMPTY.equals(s)) {
                separator = s;
                break;
            }
            if(text.contains(s)) {
                separator = s;
                break;
            }
        }

        List<String> splits;
        if(!StringUtils.isEmpty(separator)) {
            splits = Arrays.asList(text.split(separator));
        } else {
            splits = Arrays.asList(new String[] { text });
        }

        List<String> good_splits = new ArrayList<>();
        for (String s : splits) {
            if(s.length() < getChunkSize()) {
                good_splits.add(s);
            } else {
                if(good_splits.size() > 0) {
                    List<String> merged_text = mergeSplits(good_splits, separator);
                    final_chunks.addAll(merged_text);
                    good_splits.clear();
                }
                List<String> other_info = splitText(s);
                final_chunks.addAll(other_info);
            }
        }

        if(good_splits.size() > 0) {
            List<String> merged_text = mergeSplits(good_splits, separator);
            final_chunks.addAll(merged_text);
        }
        return final_chunks;
    }
}

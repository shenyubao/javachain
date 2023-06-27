package com.shenyubao.javachain.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shenyubao
 * @date 2023/6/23 09:50
 */
public class WorkPropertiesUtils {

    private static final String PROPERTIES_FILE_NAME = "javachain";
    private static final PropertiesUtils PROPERTIES_UTILS = new PropertiesUtils(PROPERTIES_FILE_NAME);

    /**
     * 通过key获取值
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * 通过key获取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String get(String key, Object defaultValue) {
        String value = PROPERTIES_UTILS.get(key);

        if (StringUtils.isEmpty(value)) {
            value = (defaultValue != null ? defaultValue.toString() : "");
        }
        return value;
    }
}

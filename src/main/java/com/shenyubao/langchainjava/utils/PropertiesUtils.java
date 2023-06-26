package com.shenyubao.langchainjava.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author shenyubao
 * @date 2023/6/23 09:50
 */
@Slf4j
@Data
public class PropertiesUtils {
    private ResourceBundle res;

    public PropertiesUtils(String bundleName) {
        try {
            res = ResourceBundle.getBundle(bundleName);
        }
        catch (Exception e) {
            log.error(String.format("load bundleName=%s error", bundleName), e);
//            throw new RuntimeException("The properties-file<" + bundleName + "> maybe not exist!", e);
        }
    }

    public String get(String key) {
        try {
            if(res == null) {
                return null;
            }
            if(!res.containsKey(key)) {
                return null;
            }
            String value = res.getString(key);
            return value;
        }
        catch (Exception e) {
            log.error(String.format("get key=%s error", key), e);
            return null;
        }
    }

    public PropertyResourceBundle getBundle() {
        return (PropertyResourceBundle) res;
    }
}

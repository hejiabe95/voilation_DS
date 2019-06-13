package com.zc.intf.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class JsonUtil {
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseArray(json, clazz);
    }
}

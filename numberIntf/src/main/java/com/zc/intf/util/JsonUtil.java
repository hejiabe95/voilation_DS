package com.zc.intf.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;
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

    public static <T> T toBeanNoCase(JSONObject jsonObject, Class<T> clazz) {

        jsonObject.entrySet().stream().map(stringObjectEntry -> {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            String lowerCastKey = key.toLowerCase();
            return new Map.Entry<String, Object>() {
                @Override
                public String getKey() {
                    return lowerCastKey;
                }

                @Override
                public Object getValue() {
                    return value;
                }

                @Override
                public Object setValue(Object value) {
                    return null;
                }
            };

        });

        return (T) jsonObject.toJavaObject(clazz);

    }


}

package com.leisen.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JSONUtil {

    /**
     * 序列化（对象转JSON字符串）
     * @param object
     * @return
     * @throws Exception
     */
    public static String object2JSON(Object object) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * 反序列化（JSON字符串转对象）
     * @param json
     * @param objType
     * @return
     * @throws Exception
     */
    public static Object JSON2Object(String json, Class objType) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, objType);
    }
}

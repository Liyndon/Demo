package com.core.util;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.ser.std.NullSerializer;
import org.codehaus.jackson.type.TypeReference;




/**
 * 基于Jackson的json的序列化和反序列化
 *
 */
public class JsonUtilTools {

	private static final Logger log = Logger.getLogger(JsonUtilTools.class);
    final static ObjectMapper objectMapper;
    
    /**
     * 是否打印美观格式
     */
    static boolean isPretty = false;
 
    static {
        StdSerializerProvider sp = new StdSerializerProvider();
        sp.setNullValueSerializer(NullSerializer.instance);
        objectMapper = new ObjectMapper(null, sp, null);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
 
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。
     * @param <T>
     * @param jsonString JSON字符串
    * @param tr TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    @SuppressWarnings("unchecked")
	public static <T> T jsonToGenericObject(String jsonString, TypeReference<T> tr) {
 
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return (T) objectMapper.readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
        }
        return null;
    }
 
    /**
     * Java对象转Json字符串
     * 
     * @param object Java对象，可以是对象，数组，List,Map等
     * @return json 字符串
     */
    @SuppressWarnings("deprecation")
	public static String toJson(Object object) {
        String jsonString = "";
        try {
            if (isPretty) {
                jsonString = objectMapper.defaultPrettyPrintingWriter().writeValueAsString(object);
            } else {
                jsonString = objectMapper.writeValueAsString(object);
            }
        } catch (Exception e) {
            log.warn("json error:" + e.getMessage());
        }
        return jsonString;
 
    }
 
    /**
     * Json字符串转Java对象
     * 
     * @param jsonString
     * @param c
     * @return
     */
    public static Object jsonToObject(String jsonString, Class<?> c) {
 
        if (jsonString == null || "".equals(jsonString)) {
            return "";
        } else {
            try {
                return objectMapper.readValue(jsonString, c);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
 
        }
        return "";
    }
}

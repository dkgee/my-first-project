package com.tank.es.utils;


import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WrapUtil
 *
 * @author chenxueyan
 * @since 16/11/17
 */
public class SearchWrapUtil {

    private static Logger LOG = Logger.getLogger(SearchWrapUtil.class);

    /**
     * wrap sourceMap to a Class object specialed
     *
     * @param sourceMap es search sourceMap
     * @return a object
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ParseException
     */
    public static Object formattedSource(Map<String, Object> sourceMap, Class<?> targetClazz) throws IllegalAccessException, InstantiationException, ClassNotFoundException,InstantiationException,IOException{
        Gson gson = new Gson();
        String json = gson.toJson(sourceMap);
        return gson.fromJson(json, targetClazz);
    }


    public static <T> T fromSourceMap(Map<String, Object> sourceMap, Class<?> aClass) throws IllegalAccessException, InstantiationException, ParseException, ClassNotFoundException {
        T object = (T) aClass.newInstance();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (sourceMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    String s = field.getType().getName();
                    if (s.equals("java.lang.Long")) {
                        field.set(object, sourceMap.get(field.getName()));
                    } else if (s.equals("long")) {
                        field.set(object, Long.valueOf(String.valueOf(sourceMap.get(field.getName()))));
                    } else if (s.equals("java.lang.Short")) {
                        field.set(object, Short.valueOf(String.valueOf(sourceMap.get(field.getName()))));
                    } else if (s.equals("java.lang.Boolean") || s.equals("java.lang.String")) {
                        field.set(object, sourceMap.get(field.getName()));
                    } else if (s.equals("java.lang.Double")) {
                        field.set(object, Double.parseDouble(String.valueOf(sourceMap.get(field.getName()))));
                    } else if (s.equals("java.util.Date")) {
                        field.set(object, DateUtil.fromLong(Long.parseLong(String.valueOf(sourceMap.get(field.getName())))));
                    } else if (s.equals("java.lang.Integer")) {
                        field.set(object, Integer.valueOf(String.valueOf(sourceMap.get(field.getName()))));
                    } else if (s.equals("java.util.Map")) {
                        field.set(object, sourceMap.get(field.getName()));
                    } else if (s.equals("java.util.ArrayList")) {
                        field.set(object, sourceMap.get(field.getName()));
                    } else if (s.equals("java.util.List")) {
                        Type type = field.getGenericType();
                        if (type instanceof ParameterizedType) {
                            ParameterizedType pType = (ParameterizedType) type;
                            Class<?> argType = (Class<?>) pType.getActualTypeArguments()[0];
                            field.set(object, fromSourceList((List<Object>) sourceMap.get(field.getName()), Class.forName(argType.getName())));
                        } else {
                            field.set(object, fromSourceList((List<Object>) sourceMap.get(field.getName()), field.getType()));
                        }

                    } else {// 非原生类型 , 采用 fromSourceMap
                        field.set(object, fromSourceMap((Map<String, Object>) sourceMap.get(field.getName()), field.getType()));

                    }

                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return object;
    }

    /**
     * Just for source List
     *
     * @param properties list of sourceMap
     * @param aClass     class<?>
     * @return
     */
    private static List fromSourceList(List<Object> properties, Class<?> aClass) throws IllegalAccessException, ParseException, InstantiationException, ClassNotFoundException {
        if (properties == null) return null;
        List result = new ArrayList();
        for (Object property : properties) {
            if (property instanceof Map) {
                result.add(fromSourceMap((Map<String, Object>) property, aClass));
            } else {
                result.add(property);
            }

        }
        return result;
    }


    private static Boolean isJavaPrimitive(String className) {
        return className.startsWith("java.lang");
    }

}

package com.atet.tvmarket.utils;

import java.lang.reflect.Field;

/**
 * Created by starrysky on 14-12-26.
 */
public class ReflectUtils {

    public static Object getFieldValue(Class<?> aClass, String fieldName, Object receiver) throws Exception {

        if (aClass == null || fieldName == null || receiver == null) {
            throw new NullPointerException("参数不能为空!");
        }

        Field field = aClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(receiver);
    }

    public static Object getFieldValueQuietly(Class<?> aClass, String fieldName, Object receiver) {

        try {
            return getFieldValue(aClass, fieldName, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

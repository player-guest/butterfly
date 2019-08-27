package com.buttongames.butterflycore.util;

public class ObjectUtils {

    public static <T> T checkNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String checkNull(String value, String defaultValue) {
        if(value==null){
            return defaultValue;
        }else if(value.equals("")){
            return defaultValue;
        }else{
            return value;
        }
    }

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean notNull(Object value) {
        return value != null;
    }
}

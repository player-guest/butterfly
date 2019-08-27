package com.buttongames.butterflycore.util;

public class ObjectUtils {

    public static <T> T checkNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean notNull(Object value) {
        return value != null;
    }
}

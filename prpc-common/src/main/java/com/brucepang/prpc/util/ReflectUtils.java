package com.brucepang.prpc.util;

/**
 * A utility class for reflection.
 * @author BrucePang
 */
public final class ReflectUtils {
    private ReflectUtils() {
    }

    public static boolean isPrimitives(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            // Original type
            return true;
        } else {
            // 包装类型
            return clazz == Boolean.class
                    || clazz == Character.class
                    || clazz == Byte.class
                    || clazz == Short.class
                    || clazz == Integer.class
                    || clazz == Long.class
                    || clazz == Float.class
                    || clazz == Double.class;
        }
    }
}

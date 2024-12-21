package com.brucepang.prpc.util;

import java.lang.reflect.Method;

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

    /**
     * Get a description of the Java method. This description includes the name of the method and the type of arguments
     * @param method method
     * @return
     */
    public static String getDesc(Method method) {
        StringBuilder desc = new StringBuilder(method.getName() + "(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; ++i) {
            desc.append(parameterTypes[i].getSimpleName());
            if (i != parameterTypes.length - 1) {
                desc.append(", ");
            }
        }
        desc.append(")");
        return desc.toString();
    }
}

package com.brucepang.prpc.util;

import java.lang.reflect.Method;

import static com.brucepang.prpc.util.StrUtil.isNotEmpty;

/**
 * @author BrucePang
 */
public interface MethodUtils {
    /**
     * get method name
     * @param clazz clazz
     * @param methodName methodName
     * @param parameterTypes parameterTypes
     * @return method name
     */
    static String getMethodName(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append(".").append(methodName).append("(");
        if (parameterTypes != null && parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(parameterTypes[i].getName());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    static Method findMethod(Class type, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            if (type != null && isNotEmpty(methodName)) {
                method = type.getDeclaredMethod(methodName, parameterTypes);
            }
        } catch (NoSuchMethodException e) {
        }
        return method;
    }

    static <T> T invokeMethod(Object object, String methodName, Object... methodParameters) {
        Class type = object.getClass();
        Class[] parameterTypes = ReflectUtils.resolveTypes(methodParameters);
        Method method = findMethod(type, methodName, parameterTypes);
        T value = null;

        if (method == null) {
            throw new IllegalStateException(
                    String.format("cannot find method %s,class: %s", methodName, type.getName()));
        }

        try {
            final boolean isAccessible = method.isAccessible();

            if (!isAccessible) {
                method.setAccessible(true);
            }
            value = (T) method.invoke(object, methodParameters);
            method.setAccessible(isAccessible);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return value;
    }

}

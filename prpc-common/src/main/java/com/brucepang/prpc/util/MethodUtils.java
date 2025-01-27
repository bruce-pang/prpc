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

}

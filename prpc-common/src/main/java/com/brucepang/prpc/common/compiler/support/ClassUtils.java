package com.brucepang.prpc.common.compiler.support;

import java.net.URI;

/**
 * ClassUtils. (Tool, Static, ThreadSafe)
 * @author BrucePang
 */
public class ClassUtils {
    public static final String CLASS_EXTENSION = ".class";

    public static final String JAVA_EXTENSION = ".java";

    private static final int JIT_LIMIT = 5 * 1024;

    private ClassUtils() {
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        return Class.forName(className, true, classLoader);
    }

    public static <T> T newInstance(String className, ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = loadClass(className, classLoader);
        return (T) clazz.newInstance();
    }

    public static String getClassName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }

    public static boolean isAssignable(Class<?> parent, Class<?> child) {
        return parent.isAssignableFrom(child);
    }

    public static String getResourcePath(Class<?> clazz, String resourceName) {
        return clazz.getResource(resourceName).getPath();
    }

    public static URI toURI(String name) {
        try {
            return new URI(name);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}

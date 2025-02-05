package com.brucepang.prpc.common.compiler.support;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

import static com.brucepang.prpc.util.CollectionUtils.ofSet;

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

    public static final Set<Class<?>> SIMPLE_TYPES = ofSet(
            Void.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class,
            BigDecimal.class,
            BigInteger.class,
            Date.class,
            Object.class,
            Duration.class);

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

    public static Class<?> forName(String[] packages, String className) {
        try {
            return classForName(className);
        } catch (ClassNotFoundException e) {
            if (packages != null && packages.length > 0) {
                for (String pkg : packages) {
                    try {
                        return classForName(pkg + "." + className);
                    } catch (ClassNotFoundException ignore) {
                    }
                }
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "char":
                return char.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "boolean[]":
                return boolean[].class;
            case "byte[]":
                return byte[].class;
            case "char[]":
                return char[].class;
            case "short[]":
                return short[].class;
            case "int[]":
                return int[].class;
            case "long[]":
                return long[].class;
            case "float[]":
                return float[].class;
            case "double[]":
                return double[].class;
            default:
        }
        try {
            return arrayForName(className);
        } catch (ClassNotFoundException e) {
            // try to load from java.lang package
            if (className.indexOf('.') == -1) {
                try {
                    return arrayForName("java.lang." + className);
                } catch (ClassNotFoundException ignore) {
                    // ignore, let the original exception be thrown
                }
            }
            throw e;
        }
    }

    private static Class<?> arrayForName(String className) throws ClassNotFoundException {
        return Class.forName(className.endsWith("[]")
                ? "[L" + className.substring(0, className.length() - 2) + ";"
                : className, true, Thread.currentThread().getContextClassLoader());
    }

    /**
     * get simple class name from qualified class name
     */
    public static String getSimpleClassName(String qualifiedName) {
        if (null == qualifiedName) {
            return null;
        }
        int i = qualifiedName.lastIndexOf('.');
        return i < 0 ? qualifiedName : qualifiedName.substring(i + 1);
    }

    /**
     * Get the code source file or class path of the Class passed in.
     *
     * @param clazz
     * @return Jar file name or class path.
     */
    public static String getCodeSource(Class<?> clazz) {
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        if (protectionDomain == null || protectionDomain.getCodeSource() == null) {
            return null;
        }

        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        URL location = codeSource.getLocation();
        if (location == null) {
            return null;
        }

        String path = location.toExternalForm();

        if (path.endsWith(".jar") && path.contains("/")) {
            return path.substring(path.lastIndexOf('/') + 1);
        }
        return path;
    }

}

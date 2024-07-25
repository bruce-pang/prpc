package com.brucepang.prpc.util;

/**
 * @author BrucePang
 */
public class ClassLoaderResourceLoader {

    public static ClassLoader getClassLoader() {
        return ClassLoaderResourceLoader.class.getClassLoader();
    }

    public static Class<?> loadClass(String className) {
        try {
            return getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> loadClass(String className, boolean initialize) {
        try {
            return Class.forName(className, initialize, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> loadClass(String className, boolean initialize, ClassLoader classLoader) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader, boolean initialize) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader, ClassLoader defaultClassLoader) {
        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className, true, defaultClassLoader);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader, ClassLoader defaultClassLoader, boolean initialize) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className, initialize, defaultClassLoader);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader, ClassLoader defaultClassLoader, boolean initialize, boolean resolve) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className, initialize, defaultClassLoader);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static Class loadClass(String className, ClassLoader classLoader, ClassLoader defaultClassLoader, boolean initialize, boolean resolve, boolean check) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className, initialize, defaultClassLoader);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

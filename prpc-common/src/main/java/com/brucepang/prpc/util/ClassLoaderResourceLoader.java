package com.brucepang.prpc.util;

import com.brucepang.prpc.common.resource.GlobalResourcesRepository;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author BrucePang
 */
public class ClassLoaderResourceLoader {
    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderResourceLoader.class);
    private static SoftReference<Map<ClassLoader, Map<String, Set<URL>>>> classLoaderResourcesCache = null;

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

    private static void setRef(URL url) {
        try {
            Field field = URL.class.getDeclaredField("ref");
            field.setAccessible(true);
            field.set(url, UUID.randomUUID().toString());
        } catch (Throwable ignore) {
        }
    }


    public static Map<ClassLoader, Set<URL>> loadResources(String fileName, Collection<ClassLoader> classLoaders) throws InterruptedException {
        Map<ClassLoader, Set<URL>> resources = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(classLoaders.size());
        for (ClassLoader classLoader : classLoaders) {
            GlobalResourcesRepository.getGlobalExecutorService().submit(() -> {
                resources.put(classLoader, loadResources(fileName, classLoader));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        return Collections.unmodifiableMap(new LinkedHashMap<>(resources));
    }

    public static Set<URL> loadResources(String fileName, ClassLoader currentClassLoader) {
        Map<ClassLoader, Map<String, Set<URL>>> classLoaderCache;
        if (classLoaderResourcesCache == null || (classLoaderCache = classLoaderResourcesCache.get()) == null) {
            synchronized (ClassLoaderResourceLoader.class) {
                if (classLoaderResourcesCache == null || (classLoaderCache = classLoaderResourcesCache.get()) == null) {
                    classLoaderCache = new ConcurrentHashMap<>();
                    classLoaderResourcesCache = new SoftReference<>(classLoaderCache);
                }
            }
        }
        if (!classLoaderCache.containsKey(currentClassLoader)) {
            classLoaderCache.putIfAbsent(currentClassLoader, new ConcurrentHashMap<>());
        }
        Map<String, Set<URL>> urlCache = classLoaderCache.get(currentClassLoader);
        if (!urlCache.containsKey(fileName)) {
            Set<URL> set = new LinkedHashSet<>();
            Enumeration<URL> urls;
            try {
                urls = currentClassLoader.getResources(fileName);
                boolean isNative = NativeUtils.isNative();
                if (urls != null) {
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        if (isNative) {
                            //In native mode, the address of each URL is the same instead of different paths, so it is necessary to set the ref to make it different
                            setRef(url);
                        }
                        set.add(url);
                    }
                }
            } catch (IOException e) {
                logger.error("Exception occurred when reading SPI definitions. SPI path: " + fileName + " ClassLoader name: " + currentClassLoader, e);
            }
            urlCache.put(fileName, set);
        }
        return urlCache.get(fileName);
    }


}

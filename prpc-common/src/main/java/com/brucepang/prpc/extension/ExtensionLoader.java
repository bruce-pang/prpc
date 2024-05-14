package com.brucepang.prpc.extension;

import com.brucepang.prpc.util.StrUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * load prpc extensions
 * when the extension is annotated with SPI, the extension will be loaded
 * @author BrucePang
 */
public class ExtensionLoader<T> {
    private static final Logger log = LoggerFactory.getLogger(ExtensionLoader.class);
    // to scan the extension path
    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    // to scan the extension path
    private static final String PRPC_DIRECTORY = "META-INF/prpc/";

    // cache the extension loader
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    // ============================== Fields ==============================
    private final Class<?> type;

    private String cacheLoaderClassName;

    private ExtensionLoader(Class<?> type){
        this.type = type;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type){
        // check the type
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        // check the annotation
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Extension type(" + type + ") is not extension, because WITHOUT @" + SPI.class.getSimpleName() + " Annotation");
        }
        // get the extension loader
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * check {@link SPI} annotation
     * @param type the extension type
     * @return true if the extension is annotated with {@link SPI}
     * @param <T>
     */
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    /**
     * load the extension classes
     * @return the extension classes
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            // get the extension name
            String value = spi.value();
            if ((value = value.trim()).length() > 0){ // String is not empty
                // get the extension classes
                String[] names = value.split(",");
                // check the extension classes
                if (names.length == 1){
                    cacheLoaderClassName = names[0];
                } else {
                    throw new IllegalArgumentException("More than 1 extension name on extension " + type.getName());
                }
            }
        }

            Map<String, Class<?>> extensionClasses = new ConcurrentHashMap<>();
            loadDirectory(extensionClasses, SERVICES_DIRECTORY);
            loadDirectory(extensionClasses, PRPC_DIRECTORY);
            return extensionClasses;

    }

    /**
     * load the extension classes
     * @param extensionClasses the extension classes
     * @param servicesDirectory the directory to load
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String servicesDirectory) {
        String classFileName = servicesDirectory + type.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources(classFileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResource(extensionClasses, classLoader, url, false);
                }
            }
        } catch (Throwable t) {
            log.error("Exception when load extension class(interface: " + type + ", description file: " + classFileName + ").", t);
        }
    }

    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceURL, boolean overridden) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
                String line;
                String clazz = null;
                while ((line = reader.readLine()) != null) {
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int i = line.indexOf('=');
                            if (i > 0) {
                                name = line.substring(0, i).trim();
                                clazz = line.substring(i + 1).trim();
                            } else {
                                clazz = line;
                            }
                            if (StrUtil.isNotEmpty(clazz)) {
                                loadClass(extensionClasses, resourceURL, Class.forName(clazz, true, classLoader), name, overridden);
                            }
                        } catch (Throwable t) {
                            IllegalStateException e = new IllegalStateException(
                                    "Failed to load extension class (interface: " + type + ", class line: " + line + ") in " + resourceURL +
                                            ", cause: " + t.getMessage(), t);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Exception occurred when loading extension class (interface: " +
                    type + ", class file: " + resourceURL + ") in " + resourceURL, t);
        }

    }


    private void loadClass(Map<String, Class<?>> extensionClasses, URL url, Class<?> clazz, String name, boolean overridden) {
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Error when load extension class(interface: " +
                    type + ", class line: " + clazz.getName() + "), class "
                    + clazz.getName() + "is not subtype of interface.");
        }
        Class<?> c = extensionClasses.get(name);
        if (c == null || overridden){
            extensionClasses.put(name, clazz);
        } else if (c != clazz){
            throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName());
        }
    }

}

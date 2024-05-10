package com.brucepang.prpc.extension;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * load prpc extensions
 * when the extension is annotated with SPI, the extension will be loaded
 * @author BrucePang
 */
public class ExtensionLoader<T> {
    // to scan the extension path
    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    // to scan the extension path
    private static final String PRPC_DIRECTORY = "META-INF/prpc/";

    // cache the extension loader
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    // ============================== Fields ==============================
    private final Class<?> type;

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


    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }
}

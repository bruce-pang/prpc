package com.brucepang.prpc.extension;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.scope.ApplicationModel;
import com.brucepang.prpc.scope.ScopeModel;
import com.brucepang.prpc.util.Holder;
import com.brucepang.prpc.util.StrUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final ExtensionMgt extensionMgt;
    private final ScopeModel scopeModel;
    private final ExtensionInjector injector;
    private String cacheLoaderClassName;
    private List<ExtensionPostProcessor> extensionPostProcessors;
    //=============================== Caches ================================
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private AtomicBoolean destroyed = new AtomicBoolean();

    public ExtensionLoader(Class<?> type, ExtensionMgt extensionMgt, ScopeModel scopeModel) {
        this.type = type; // the extension type
        this.extensionMgt = extensionMgt; // the extension management
        // todo: post process load
        // todo: initialize strategy
        // todo: injector

        this.injector = extensionMgt.getExtensionInjector();
        this.scopeModel = scopeModel;
    }



    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return ApplicationModel.defaultModel().getDefaultModule().getExtensionLoader(type);
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
            // todo: strategy to load the extension classes
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
                Collections.list(urls).stream().forEach(url -> loadResource(extensionClasses, classLoader, url, false));
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

    public T getExtension(String cacheLoaderClassName) {
        try {
            // load the extension classes
            Map<String, Class<?>> extensionClasses = loadExtensionClasses();
            // get the extension class
            Class<?> extensionClass = extensionClasses.get(cacheLoaderClassName);
            if (extensionClass == null) {
                throw new IllegalArgumentException("No such extension of name " + cacheLoaderClassName);
            }
            // create the extension instance
            return (T) extensionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create extension instance.", e);
        }
    }

    /**
     * get the extension instance
     * @param name the extension name
     * @param wrap whether to wrap the extension
     * @return
     */
    public T getExtension(String name, boolean wrap) {
        final Holder<Object> holder = getOrCreateHolder(name);
        Object instance = holder.get();
        if (null == instance){
            synchronized (holder){
                instance = holder.get();
                if (null == instance){
                    instance = createExtension(name, wrap);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    private T createExtension(String name, boolean wrap) {
        // getExtensionClasses(): Loads all implementations of the current type extendpoint
        // then get the extension class by name
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null){
            throw new IllegalArgumentException("No such extension of name " + name);
        }
        // Create an extension instance
        T instance = null;
        try {
            instance = (T) clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    clazz + ") could not be instantiated: " + e.getMessage(), e);
        }

        // Inject dependencies
        injectExtension(instance);

        // perform post processing
        if (wrap) {
            instance = postProcessAfterInitialization(instance, name);
        }

        return instance;
    }

    private T injectExtension(T instance) {
        try {
            // todo: inject extension instance

        } catch (Exception e) {
            throw new IllegalStateException("Failed to inject extension instance(name: " + "name" + ", class: " +
                    instance.getClass() + "): " + e.getMessage(), e);
        }
        return instance;
    }

    private T postProcessAfterInitialization(T instance, String name) {
        // ExtensionPostProcessor is responsible for post-processing
        try {
            if (extensionPostProcessors != null) {
                for (ExtensionPostProcessor processor : extensionPostProcessors) {
                    instance = (T) processor.postProcessAfterInitialization(instance, name);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to post process extension instance(name: " + name + ", class: " +
                    instance.getClass() + "): " + e.getMessage(), e);
        }
    }

    private Map<String, Class<?>> getExtensionClasses() {
        // Once the extension point is loaded, it is cached into cachedClasses
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null){
            synchronized (classes){
                classes = cachedClasses.get();
                if (classes == null){
                    // Load all implementations of the current type extension point
                    classes = loadExtensionClasses();
                    // Cache the loaded extension point
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    /**
     * get the extension instance
     * @param name the extension name
     * @return the extension instance in Holder
     */
    private Holder<Object> getOrCreateHolder(String name){
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null){
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        return holder;
    }

    /**
     * Check whether the current ExtensionLoader has been destroyed
     */
    private void checkDestroyed() {
        if (destroyed.get()) {
            throw new IllegalStateException("ExtensionLoader is destroyed: " + this.type);
        }
    }


}

package com.brucepang.prpc.extension;

import com.brucepang.prpc.beans.strategy.InstantiationStrategy;
import com.brucepang.prpc.common.compiler.Compiler;
import com.brucepang.prpc.common.lifecycle.Lifecycle;
import com.brucepang.prpc.extension.inject.DisableInject;
import com.brucepang.prpc.extension.inject.ScopeModelAware;
import com.brucepang.prpc.extension.loader_strategy.LoadingStrategy;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.ScopeModel;
import com.brucepang.prpc.scope.model.ScopeModelAccessor;
import com.brucepang.prpc.util.CollectionUtils;
import com.brucepang.prpc.util.Holder;
import com.brucepang.prpc.util.ReflectUtils;
import com.brucepang.prpc.util.StrUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

/**
 * load prpc extensions
 * when the extension is annotated with SPI, the extension will be loaded
 *
 * @author BrucePang
 */
public class ExtensionLoader<T> {
    private static final Logger log = LoggerFactory.getLogger(ExtensionLoader.class);

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

    private final Holder<Object> cachedAdaptiveInstance = new Holder<>();

    private volatile Class<?> cachedAdaptiveClass = null;

    private static final List<String> ignoredInjectMethodsDesc = getIgnoredInjectMethodsDesc();

    private static SoftReference<Map<URL, List<String>>> urlListMapCache = new SoftReference<>(
            new ConcurrentHashMap<>());

    private InstantiationStrategy instantiationStrategy;

    // cache the extension instances
    private final ConcurrentMap<Class<?>, Object> extensionInstances = new ConcurrentHashMap<>(64);

    private String cachedDefaultName;

    private static final Pattern NAME_SEPARATOR         = Pattern.compile("\\s*[,]+\\s*");

    // loader strategy
    private static volatile LoadingStrategy[] strategies = loadLoadingStrategies();

    /**
     * Loads and returns a list of all {@link LoadingStrategy Loading Strategies} implementations discovered through the {@link ServiceLoader} mechanism.
     * @return an array of {@link LoadingStrategy Loading Strategies}
     */
    private static LoadingStrategy[] loadLoadingStrategies() {
        return stream(load(LoadingStrategy.class).spliterator(), false).sorted()
                .toArray(LoadingStrategy[]::new);
    }

    public ExtensionLoader(Class<?> type, ExtensionMgt extensionMgt, ScopeModel scopeModel) {
        this.type = type; // the extension type
        this.extensionMgt = extensionMgt; // the extension management
        this.extensionPostProcessors = extensionMgt.getExtensionPostProcessors();
        initInstantiationStrategy();
        this.injector = (type == ExtensionInjector.class) ? null : ExtensionLoader.getExtensionLoader(ExtensionInjector.class).getAdaptiveExtension();
        this.scopeModel = scopeModel;
    }

    private void initInstantiationStrategy() {
        instantiationStrategy = extensionPostProcessors.stream()
                .filter(extensionPostProcessor -> extensionPostProcessor instanceof ScopeModelAccessor)
                .map(extensionPostProcessor -> new InstantiationStrategy(
                        (ScopeModelAccessor) extensionPostProcessor)).findFirst()
                .orElse(new InstantiationStrategy());
    }

    private Object createExtensionInstance(Class<?> type) throws ReflectiveOperationException {
        return instantiationStrategy.instantiate(type);
    }

    private static List<String> getIgnoredInjectMethodsDesc() {
        List<String> ignoreInjectMethodsDesc = new ArrayList<>();
        Arrays.stream(ScopeModelAware.class.getMethods()).map(ReflectUtils::getDesc)
                .forEach(ignoreInjectMethodsDesc::add);
        Arrays.stream(ExtensionAccessorAware.class.getMethods()).map(ReflectUtils::getDesc)
                .forEach(ignoreInjectMethodsDesc::add);
        return ignoreInjectMethodsDesc;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return ApplicationModel.defaultModel().getDefaultModule().getExtensionLoader(type);
    }

    /**
     * check {@link SPI} annotation
     *
     * @param type the extension type
     * @param <T>
     * @return true if the extension is annotated with {@link SPI}
     */
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    /**
     * load the extension classes
     *
     * @return the extension classes
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        checkDestroyed();
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            // get the extension name
            String value = spi.value();
            if ((value = value.trim()).length() > 0) { // String is not empty
                // get the extension classes
                String[] names = value.split(",");
                // check the extension classes
                if (names.length == 1) {
                    cacheLoaderClassName = names[0];
                } else {
                    throw new IllegalArgumentException("More than 1 extension name on extension " + type.getName());
                }
            }
        }

        Map<String, Class<?>> extensionClasses = new ConcurrentHashMap<>();
        Optional.ofNullable(strategies).ifPresent(strategies -> {
            for (LoadingStrategy strategy : strategies) {
                if (strategy == null) {
                    continue;
                }
                String directory = strategy.directory();
                if (StrUtil.isEmpty(directory)) {
                    continue;
                }
                loadDirectory(extensionClasses, directory);
            }
        });
        return extensionClasses;

    }

    /**
     * 5.load the extension classes
     *
     * @param extensionClasses  the extension classes
     * @param servicesDirectory the directory to load
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String servicesDirectory) {
        String classFileName = servicesDirectory + type.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources(classFileName);
            if (urls != null) {
                // 6.load the extension classes
                Collections.list(urls).stream().forEach(url -> loadResource(extensionClasses, classLoader, url, false, null, null, null));
            }
        } catch (Throwable t) {
            log.error("Exception when load extension class(interface: " + type + ", description file: " + classFileName + ").", t);
        }
    }

    /**
     * 5.load the extension classes
     *
     * @param extensionClasses  the extension classes
     * @param strategy the loading strategy
     * @param type the extension type
     */
    private void loadDirectoryInternal(Map<String, Class<?>> extensionClasses, LoadingStrategy strategy, String type) throws InterruptedException {
        String fileName = strategy.directory() + type;
        try{
            List<ClassLoader> classLoadersToLoad = new LinkedList<>();
            // Load from ExtensionClassLoader's ClassLoader first
            if (strategy.preferExtensionClassLoader()) {
                ClassLoader extensionClassLoader = ExtensionLoader.class.getClassLoader();
                if (ClassLoader.getSystemClassLoader() != extensionClassLoader) {
                    classLoadersToLoad.add(extensionClassLoader);
                }
            }

            // Load from Scope Model
            Set<ClassLoader> classLoaders = scopeModel.getClassLoaders();
            if (CollectionUtils.isEmpty(classLoaders)) {
                Enumeration<java.net.URL> resources = ClassLoader.getSystemResources(fileName);
                if (resources != null) {
                    while (resources.hasMoreElements()) {
                        loadResource(extensionClasses, null, resources.nextElement(),
                                strategy.overridden(), strategy.includedPackages(),
                                strategy.excludedPackages(),
                                strategy.onlyExtensionClassLoaderPackages());
                    }
                }
            } else {
                classLoadersToLoad.addAll(classLoaders);
            }

        } catch (Throwable e) {
            log.error("Exception when load extension class(interface: " + type + ", description file: " + fileName + ").", e);
        }
    }

    /**
     * 6.load the extension classes
     *
     * @param extensionClasses the extension classes
     * @param classLoader      the class loader
     * @param resourceURL      the resource URL
     * @param overridden       whether the extension is overridden
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceURL, boolean overridden,  String[] includedPackages, String[] excludedPackages,
                              String[] onlyExtensionClassLoaderPackages) {
        try {
            // Load the resource file and parse the fully qualified class name of the extension point
            List<String> newContentList = getResourceContent(resourceURL);
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

    private void loadFromClass(Map<String, Class<?>> extensionClasses, boolean overridden,
                               Set<java.net.URL> urls, ClassLoader classLoader,
                               String[] includedPackages, String[] excludedPackages,
                               String[] onlyExtensionClassLoaderPackages) {
        if (CollectionUtils.isNotEmpty(urls)) {
            for (java.net.URL url : urls) {
                loadResource(extensionClasses, classLoader, url, overridden, includedPackages,
                        excludedPackages, onlyExtensionClassLoaderPackages);
            }
        }
    }

    private List<String> getResourceContent(java.net.URL resourceURL) {
        // Get the cache, create a new one if it's null
        Map<URL, List<String>> urlListMap;
        if (urlListMapCache.get() == null) {
            urlListMap = new ConcurrentHashMap<>();
            urlListMapCache = new SoftReference<>(urlListMap);
        } else {
            urlListMap = urlListMapCache.get();
        }

        // Load the resource file and parse the fully qualified class name of the extension point
        return urlListMap.computeIfAbsent(resourceURL, url -> {
            try (Stream<String> lines = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).lines()) {
                return lines.collect(Collectors.toList());
            } catch (Throwable t) {
                log.error("Exception occurred when loading extension class (interface: " +
                        type + ", class file: " + resourceURL + ") in " + resourceURL, t);
                return Collections.emptyList();
            }
        });
    }


    private void loadClass(Map<String, Class<?>> extensionClasses, URL url, Class<?> clazz, String name, boolean overridden) {
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Error when load extension class(interface: " +
                    type + ", class line: " + clazz.getName() + "), class "
                    + clazz.getName() + "is not subtype of interface.");
        }
        Class<?> c = extensionClasses.get(name);
        if (c == null || overridden) {
            extensionClasses.put(name, clazz);
        } else if (c != clazz) {
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
     * 1. Load all the extension points at startup
     * get the extension instance
     *
     * @param name the extension name
     * @param wrap whether to wrap the extension
     * @return
     */
    public T getExtension(String name, boolean wrap) {
        final Holder<Object> holder = getOrCreateHolder(name);
        Object instance = holder.get();
        if (null == instance) {
            synchronized (holder) {
                instance = holder.get();
                if (null == instance) {
                    // 2. create the extension instance when needed
                    instance = createExtension(name, wrap);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * create the extension instance
     *
     * @param name the extension name
     * @param wrap whether to wrap the extension
     * @return the extension instance
     */
    private T createExtension(String name, boolean wrap) {
        // getExtensionClasses(): Loads all implementations of the current type extendpoint
        // then get the extension class by name
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new IllegalArgumentException("No such extension of name " + name);
        }
        // Create an extension instance
        T instance = null;
        try {
            // Instancing (singleton)
            instance = (T) extensionInstances.get(clazz);
            if (instance == null) {
                extensionInstances.putIfAbsent(clazz, createExtensionInstance(clazz));
                instance = (T) extensionInstances.get(clazz);
                // Inject dependencies
                injectExtension(instance);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    clazz + ") could not be instantiated: " + e.getMessage(), e);
        }


        // perform post processing
        if (wrap) {
            instance = postProcessAfterInitialization(instance, name);
        }

        return instance;
    }

    private T injectExtension(T instance) {
        if (injector == null) {
            return instance;
        }
        for (Method method : instance.getClass().getMethods()) {
            // Skip methods that don't require dependency injection
            // 1.not a setter method
            // 2.has a DisableInject.class annotation
            // 3.the method is declared in ScopeModelAware
            if (
                    !isSetter(method)
                            || method.isAnnotationPresent(DisableInject.class)
                            || method.getDeclaringClass() == ScopeModelAware.class
            ) {
                continue;
            }

            if (
                    instance instanceof ScopeModelAware
                            || instance instanceof ExtensionAccessorAware
            ) {
                if (ignoredInjectMethodsDesc.contains(ReflectUtils.getDesc(method))) { // 4. Skip the method declared in ignoredInjectMethodsDesc list when the instance is ScopeModelAware or ExtensionAccessorAware
                    continue;
                }
            }

            if (ReflectUtils.isPrimitives(method.getParameterTypes()[0])){ // 5. Skip the basic data type
                continue;
            }

            try {
                String property = getSetterProperty(method);
                Object object = injector.getInstance(method.getParameterTypes()[0], property);
                if (object != null) {
                    method.invoke(instance, object);
                }
            } catch (Exception e) {
                log.error("Failed to inject via method " + method.getName() + " of interface " + type.getName() + ": " + e.getMessage(), e);
            }
        }
        return instance;
    }

    /**
     * get the setter property
     *
     * @param method the method
     * @return the setter property
     */
    private String getSetterProperty(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("set")) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        }
        return null;
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

    /**
     * 3.get the extension classes
     *
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        // Once the extension point is loaded, it is cached into cachedClasses
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (classes) {
                classes = cachedClasses.get();
                if (classes == null) {
                    // 4.Load all implementations of the current type extension point
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
     *
     * @param name the extension name
     * @return the extension instance in Holder
     */
    private Holder<Object> getOrCreateHolder(String name) {
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        return holder;
    }

    @SuppressWarnings("unchecked")
    public T getAdaptiveExtension() {
        checkDestroyed();
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            synchronized (cachedAdaptiveInstance) {
                instance = cachedAdaptiveInstance.get();
                if (instance == null) {
                    try {
                        instance = createAdaptiveExtension();
                        cachedAdaptiveInstance.set(instance);
                    } catch (Throwable t) {
                        throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
                    }
                }
            }
        }

        return (T) instance;
    }

    private T createAdaptiveExtension() {
        try {
            T instance = (T) getAdaptiveExtensionClass().newInstance();
            instance = postProcessBeforeInitialization(instance, null);
            injectExtension(instance);
            instance = postProcessAfterInitialization(instance, null);
            initExtension(instance);
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Can't create adaptive extension " + type + ", cause: " + e.getMessage(), e);
        }
    }

    private void initExtension(T instance) {
        if (instance instanceof Lifecycle) {
            Lifecycle lifecycle = (Lifecycle) instance;
            lifecycle.initialize();
        }
    }

    private T postProcessBeforeInitialization(T instance, String name) throws Exception {
        if (extensionPostProcessors != null) {
            for (ExtensionPostProcessor processor : extensionPostProcessors) {
                instance = (T) processor.postProcessBeforeInitialization(instance, name);
            }
        }
        return instance;
    }

    private Class<?> getAdaptiveExtensionClass() {
        getExtensionClasses();
        if (cachedAdaptiveClass != null) {
            return cachedAdaptiveClass;
        }
        return cachedAdaptiveClass = createAdaptiveExtensionClass();
    }


    private Class<?> createAdaptiveExtensionClass() {
        // Adaptive Classes' ClassLoader should be the same with Real SPI interface classes' ClassLoader
        ClassLoader classLoader = type.getClassLoader();
        // Generate the adaptive class
        String code = createAdaptiveExtensionClassCode();
        Compiler compiler = extensionMgt.getExtensionLoader(Compiler.class).getAdaptiveExtension();
        return compiler.compile(code, classLoader);
    }

    private String createAdaptiveExtensionClassCode() {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(type.getPackage().getName()).append(";");
        code.append("import ").append(ExtensionLoader.class.getName()).append(";");
        code.append("public class ").append(type.getSimpleName()).append("$Adaptive implements ").append(type.getCanonicalName()).append(" {");
        code.append("public ").append(type.getSimpleName()).append("$Adaptive(){}");
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            code.append("public ").append(method.getReturnType().getCanonicalName()).append(" ").append(method.getName()).append("(");
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) {
                    code.append(",");
                }
                code.append(parameterTypes[i].getCanonicalName()).append(" arg").append(i);
            }
            code.append("){");
            code.append("String methodName = \"").append(method.getName()).append("\";");
            code.append("String[] argTypes = new String[").append(parameterTypes.length).append("];");
            for (int i = 0; i < parameterTypes.length; i++) {
                code.append("argTypes[").append(i).append("] = arg").append(i).append(".getClass().getName();");
            }
            code.append("ExtensionLoader loader = ExtensionLoader.getExtensionLoader(").append(type.getCanonicalName()).append(".class);");
            code.append("ExtensionAccessor accessor = loader.getExtensionAccessor();");
            code.append("return (").append(method.getReturnType().getCanonicalName()).append(")accessor.invoke(methodName, argTypes, new Object[]{").append(Stream.of(parameterTypes).map(p -> "arg" + p).collect(Collectors.joining(","))).append("});");
            code.append("}");
        }
        code.append("}");
        return code.toString();
    }


    /**
     * Check whether the current ExtensionLoader has been destroyed
     */
    private void checkDestroyed() {
        if (destroyed.get()) {
            throw new IllegalStateException("ExtensionLoader is destroyed: " + this.type);
        }
    }

    /**
     * check whether the method is setter
     *
     * @param method the method
     * @return true if the method is setter
     */
    private boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getParameterTypes().length == 1
                && Modifier.isPublic(method.getModifiers());
    }

    /**
     * get the supported extensions
     * @return the supported extensions
     */
    public Set<String> getSupportedExtensions() {
        checkDestroyed();
        Map<String, Class<?>> classes = getExtensionClasses();
        return Collections.unmodifiableSet(new TreeSet<>(classes.keySet()));
    }

    /**
     * extract and cache default extension name if exists
     */
    private void cacheDefaultExtensionName() {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            String value = spi.value();
            if ((value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                if (names.length == 1) {
                    cachedDefaultName = names[0];
                }
            }
        }
    }

    /**
     * get default extension
     * @return the default extension or null if not exists
     */
    public T getDefaultExtension() {
        getExtensionClasses();
        if (StrUtil.isBlank(cachedDefaultName) || "true".equals(cachedDefaultName)) {
            return null;
        }
        return getExtension(cachedDefaultName);
    }

    public boolean hasExtension(String name) {
        checkDestroyed();
        if (StrUtil.isEmpty(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        Class<?> c = this.getExtensionClass(name);
        return c != null;
    }

    private Class<?> getExtensionClass(String name) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Extension name == null");
        }
        return getExtensionClasses().get(name);
    }

}

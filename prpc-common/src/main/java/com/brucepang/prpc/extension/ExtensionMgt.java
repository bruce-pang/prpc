package com.brucepang.prpc.extension;

import com.brucepang.prpc.scope.model.ScopeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author BrucePang
 */
public class ExtensionMgt implements ExtensionAccessor {

    private final ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoadersMap = new ConcurrentHashMap<>(64);

    private final ExtensionScope scope;

    private final ExtensionMgt parent;

    private final ScopeModel scopeModel;

    private final ConcurrentMap<Class<?>, ExtensionScope> extensionScopeMap = new ConcurrentHashMap<>(64);

    private final List<ExtensionPostProcessor> extensionPostProcessors = new ArrayList<>();

    public ExtensionMgt(ExtensionScope scope, ExtensionMgt parent, ScopeModel scopeModel) {
        this.scope = scope;
        this.parent = parent;
        this.scopeModel = scopeModel;
    }


    @Override
    public ExtensionMgt getExtensionMgt() {
        return this;
    }


    @Override
    public <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (null == type) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }
        if (!hasExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an extension, because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
        }
        // 1-find in local cache
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoadersMap.get(type);
        //
        ExtensionScope scope = extensionScopeMap.get(type);
        if (scope == null) {
            SPI annotation = type.getAnnotation(SPI.class);
            scope = annotation.scope();
            extensionScopeMap.put(type, scope);// add default scope for type.
        }

       // 2.check loader is using in self?
        if (loader == null && scope == ExtensionScope.SELF){
            // create an instance in self scope
            loader = createExtensionLoaderForSelf(type);
        }

        // 3.find in parent
        if (null == loader && this.parent != null){
            loader = this.parent.getExtensionLoader(type);
        }

        // 4. create it
        if (loader == null){
           loader = createExtensionLoader(type);
        }
        return loader;
    }

    private <T> ExtensionLoader<T> createExtensionLoader(Class<T> type) {
        ExtensionLoader<T> loader = null;
        if (isScopeMatched(type)) {
            // if scope is matched, just create it
            loader = createExtensionLoaderForSelf(type);
        }
        return loader;
    }

    /**
     * create extension loader for self
     * @param type the extension type
     * @return the extension loader
     */
    private <T> ExtensionLoader<T> createExtensionLoaderForSelf(Class<T> type) {
        ExtensionLoader<T> loader;
        extensionLoadersMap.putIfAbsent(type, new ExtensionLoader<T>(type, this, scopeModel));
        loader = (ExtensionLoader<T>) extensionLoadersMap.get(type);
        return loader;
    }

    private boolean isScopeMatched(Class<?> type) {
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        return defaultAnnotation.scope().equals(scope);
    }


    private static boolean hasExtensionAnnotation(Class<?> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    /**
     * Add an extension post-processor
     * @param processor the extension post-processor
     */
    public void addExtensionPostProcessor(ExtensionPostProcessor processor) {
        if (!this.extensionPostProcessors.contains(processor)) {
            this.extensionPostProcessors.add(processor);
        }
    }

    /**
     * Get the extension post-processors
     * @return the extension post-processors
     */
    public List<ExtensionPostProcessor> getExtensionPostProcessors() {
        return extensionPostProcessors;
    }
}

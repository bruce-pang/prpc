package com.brucepang.prpc.extension;

/**
 * A Post-processor called before or after extension initialization.
 */
public interface ExtensionPostProcessor<T> {

    default T postProcessBeforeInitialization(T instance, String name) throws Exception {
        return instance;
    }

    default T postProcessAfterInitialization(T instance, String name) throws Exception {
        return instance;
    }

}

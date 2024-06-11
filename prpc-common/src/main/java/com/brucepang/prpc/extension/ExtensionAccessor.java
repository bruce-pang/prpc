package com.brucepang.prpc.extension;

/**
 * @description SPI extension can implement this aware interface to obtain appropriate {@link ExtensionAccessor} instance.
 */
public interface ExtensionAccessor {
    ExtensionMgt getExtensionMgt();

    default <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return getExtensionMgt().getExtensionLoader(type);
    }
    default <T> T getExtension(Class<T> type, String name) {
        ExtensionLoader<T> extensionLoader = getExtensionLoader(type);
        return extensionLoader != null ? extensionLoader.getExtension(name) : null;
    }

}

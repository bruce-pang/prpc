package com.brucepang.prpc.extension;

/**
 * @author BrucePang
 */
public class SPIExtensionInjector implements ExtensionInjector {

    @Override
    public <T> T getInstance(Class<T> type, String name) {
        if (!type.isInterface() || !type.isAnnotationPresent(SPI.class)) {
            return null;
        }
        ExtensionLoader<T> extensionLoader = ExtensionLoader.getExtensionLoader(type);
        if (null == extensionLoader) {
            return null;
        }
        // get the extension instance
        return extensionLoader.getExtension(name);
    }
}

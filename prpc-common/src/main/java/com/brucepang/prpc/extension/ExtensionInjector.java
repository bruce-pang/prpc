package com.brucepang.prpc.extension;

/**
 * An injector to provide resources for SPI extension.
 */
@SPI(scope = ExtensionScope.SELF)
public interface ExtensionInjector extends ExtensionAccessorAware {

    /**
     * Get instance of specify type and name.
     *
     * @param type object type.
     * @param name object name.
     * @return object instance.
     */
    <T> T getInstance(final Class<T> type, final String name);

    @Override
    default void setExtensionAccessor(final ExtensionAccessor extensionAccessor) {
    }
}
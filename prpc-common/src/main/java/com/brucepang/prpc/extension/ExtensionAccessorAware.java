package com.brucepang.prpc.extension;

/**
 * @description SPI extension can implement this aware interface to obtain appropriate {@link ExtensionAccessor} instance.

 */
public interface ExtensionAccessorAware {
    void setExtensionAccessor(final ExtensionAccessor extensionAccessor);
}

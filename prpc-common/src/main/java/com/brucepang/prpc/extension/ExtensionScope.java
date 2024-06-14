package com.brucepang.prpc.extension;

/**
 * Extension SPI scope.
 * @see SPI
 */
public enum ExtensionScope {
    /**
     * Global scope. The extension instance is shared with Applications and modules.
     */
    GLOBAL,
    /**
     * Application scope. The extension instance is shared among all modules of the application, different applications create different extension instances.
     */
    APPLICATION,
    /**
     * Module scope. The extension instance is shared among all modules of the application, different modules create different extension instances.
     */
    MODULE,
    /**
     * Self scope. The extension instance is unique in the current module.
     */
    SELF
}
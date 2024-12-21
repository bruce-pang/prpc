package com.brucepang.prpc.extension.loader_strategy;

import com.brucepang.prpc.lang.Prioritized;

/**
 * @author BrucePang
 */
public interface LoadingStrategy extends Prioritized {
    /**
     * when spi is loaded by prpc global classloader, make sure all LoadingStrategy should load this spi
     */
    String ALL = "ALL";

    /**
     * The directory where the extension is located
     * @return directory
     */
    String directory();

    /**
     * Whether the extension classloader is preferred
     * @return true if the extension classloader is preferred
     */
    default boolean preferExtensionClassLoader() {
        return false;
    }

    /**
     * The packages that should not be loaded
     * @return excluded packages
     */
    default String[] excludedPackages() {
        return null;
    }

    /**
     * The packages that should be loaded
     * @return included packages
     */
    default String[] includedPackages() {
        // default match all
        return null;
    }


    /**
     * To restrict some class that should load from PRPC's ClassLoader.
     * For example, we can restrict the class declaration in `com.brucepang.prpc` package should
     * be loaded from PRPC's ClassLoader and users cannot declare these classes.
     *
     * @return class packages should load
     * @since 1.4.1
     */
    default String[] onlyExtensionClassLoaderPackages() {
        return new String[]{};
    }

    /**
     * Indicates whether the current {@link LoadingStrategy} supports overriding other lower prioritized instances.
     * @return if supports, return <code>true</code>, or <code>false</code>
     */
    default boolean overridden() {
        return false;
    }

    /**
     * Get the simple name of the strategy
     * @return the simple name of the strategy
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

}

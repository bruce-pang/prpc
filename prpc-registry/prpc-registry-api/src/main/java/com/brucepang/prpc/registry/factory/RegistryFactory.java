package com.brucepang.prpc.registry.factory;

import java.net.URL;
import java.rmi.registry.Registry;
/**
 * RegistryFactory. (SPI, Singleton, ThreadSafe)
 */
public interface RegistryFactory {

    /**
     * Connect to the registry
     *
     * @param url registry url
     * @return registry
     */
    Registry getRegistry(URL url);

}
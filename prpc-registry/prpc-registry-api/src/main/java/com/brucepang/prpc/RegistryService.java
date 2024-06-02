package com.brucepang.prpc;

import java.net.InetSocketAddress;

/**
 * RegistryService. (SPI, Prototype, ThreadSafe)
 *
 */
public interface RegistryService {
    /**
     * Register a service with its inet socket address
     *
     * @param serviceName the name of the service
     * @param inetSocketAddress the inet socket address of the service
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * Lookup a service by its name
     *
     * @param serviceName the name of the service
     * @return the inet socket address of the service
     */
    InetSocketAddress lookup(String serviceName);

    /**
     * Heartbeat to keep the connection with the registry center
     *
     * @param serviceName the name of the service
     */
    void heartbeat(String serviceName);

    /**
     * Push the service list to the client
     *
     * @param serviceName the name of the service
     * @return the service list
     */
    String push(String serviceName);

    /**
     * Pull the service list from the registry center
     *
     * @param serviceName the name of the service
     * @return the service list
     */
    String pull(String serviceName);
}
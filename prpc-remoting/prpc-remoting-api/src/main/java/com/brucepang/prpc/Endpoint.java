package com.brucepang.prpc;

import com.brucepang.prpc.common.URL;

import java.net.InetSocketAddress;

/**
 * Endpoint. (API/SPI, Prototype, ThreadSafe)
 *
 * @author BrucePang
 */
public interface Endpoint {
    /**
     * get url
     * @return
     */
    URL getUrl();

    /**
     * get the service interface
     * @return
     */
    Class<?> getInterface();

    /**
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * send message.
     *
     * @param message
     * @throws RemotingException
     */
    void send(Object message) throws RemotingException;

    /**
     * close the channel.
     */
    void close();

    /**
     * Graceful close the channel.
     */
    void close(int timeout);

}

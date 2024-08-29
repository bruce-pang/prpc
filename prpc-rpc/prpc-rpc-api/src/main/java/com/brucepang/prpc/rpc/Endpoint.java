package com.brucepang.prpc.rpc;

import com.brucepang.prpc.common.URL;

import java.net.InetSocketAddress;

/**
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

}

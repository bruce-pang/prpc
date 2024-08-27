package com.brucepang.prpc.rpc;

import com.brucepang.prpc.common.URL;

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
}

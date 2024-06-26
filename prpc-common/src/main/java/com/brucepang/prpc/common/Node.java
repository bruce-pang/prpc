package com.brucepang.prpc.common;

import com.brucepang.prpc.URL;

/**
 * @author BrucePang
 */
public interface Node {

    /**
     * get url.
     *
     * @return url.
     */
    URL getUrl();

    /**
     * is available.
     *
     * @return available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();
}

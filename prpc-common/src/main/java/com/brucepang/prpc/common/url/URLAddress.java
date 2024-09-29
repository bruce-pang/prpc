package com.brucepang.prpc.common.url;

import java.io.Serializable;

/**
 * @author BrucePang
 */
public class URLAddress implements Serializable {
    private static final long serialVersionUID = 369088948888309750L;
    protected String host;
    protected int port;

    // cache
    protected transient String rawAddress;
    protected transient long timestamp;

    public URLAddress(String host, int port) {
        this(host, port, null);
    }

    public URLAddress(String host, int port, String rawAddress) {
        this.host = host;
        port = Math.max(port, 0);
        this.port = port;

        this.rawAddress = rawAddress;
        this.timestamp = System.currentTimeMillis();
    }
}

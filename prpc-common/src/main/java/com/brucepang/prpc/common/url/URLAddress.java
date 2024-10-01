package com.brucepang.prpc.common.url;

import java.io.Serializable;
import java.util.Objects;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRawAddress() {
        return rawAddress;
    }

    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URLAddress that = (URLAddress) o;
        return port == that.port && timestamp == that.timestamp && Objects.equals(host, that.host) && Objects.equals(rawAddress, that.rawAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, rawAddress, timestamp);
    }

    @Override
    public String toString() {
        return "URLAddress{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", rawAddress='" + rawAddress + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

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

    public URLAddress setHost(String host) {
        return new URLAddress(host, port, null);
    }

    public int getPort() {
        return port;
    }

    public URLAddress setPort(int port) {
        return new URLAddress(host, port, null);
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

    protected String getAddress(String host, int port) {
        return port <= 0 ? host : host + ':' + port;
    }

    public String getAddress() {
        if (rawAddress == null) {
            rawAddress = getAddress(getHost(), getPort());
        }
        return rawAddress;
    }

    public URLAddress setAddress(String host, int port) {
        return new URLAddress(host, port, rawAddress);
    }

    public String getProtocol() {
        return "";
    }

    public URLAddress setProtocol(String protocol) {
        return this;
    }

    public String getUsername() {
        return "";
    }

    public URLAddress setUsername(String username) {
        return this;
    }

    public String getPassword() {
        return "";
    }

    public URLAddress setPassword(String password) {
        return this;
    }

    public String getPath() {
        return "";
    }

    public URLAddress setPath(String path) {
        return this;
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

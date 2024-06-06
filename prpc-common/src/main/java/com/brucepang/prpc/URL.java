package com.brucepang.prpc;

import java.io.Serializable;
import java.util.Map;

/**
 * @author BrucePang
 * @description URL who is based on JDK URL and is extended and enhanced for the features of the distributed service framework.
 */
public class URL implements Serializable {
    private static final long serialVersionUID = -5405446601928993115L;

    protected String protocol;

    protected String host;

    protected int port;

    protected String path;

    private final Map<String,String> params;

    private final Map<String, Map<String, String>> parameterTypes; //参数类型

    protected URL() {
        this.protocol = null;
        this.host = null;
        this.port = 0;
        this.path = null;
        this.params = null;
        this.parameterTypes = null;
    }

    public URL(String protocol, String host, int port) {
        this(protocol, host, port, null, null, null);
    }

    public URL(String protocol, String host, int port, String path) {
        this(protocol, host, port, path, null, null);
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> params, Map<String, Map<String, String>> parameterTypes) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.params = params;
        this.parameterTypes = parameterTypes;
    }

}

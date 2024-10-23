package com.brucepang.prpc.common.url;

import com.brucepang.prpc.common.URL;

import java.util.Map;

/**
 * @author BrucePang
 */
public abstract class ServiceAddressURL extends URL {

    protected final transient URL consumerURL;

    public ServiceAddressURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters,
            URL consumerURL, URL consumerURL1) {
        super(protocol, username, password, host, port, path, parameters);
        this.consumerURL = consumerURL1;
    }
}

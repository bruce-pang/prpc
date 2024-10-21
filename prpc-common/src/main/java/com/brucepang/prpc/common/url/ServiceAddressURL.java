package com.brucepang.prpc.common.url;

import com.brucepang.prpc.common.URL;

import java.util.Map;

/**
 * @author BrucePang
 */
public abstract class ServiceAddressURL extends URL {

    public ServiceAddressURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters,
            URL consumerURL) {
        super(protocol, username, password, host, port, path, parameters);
    }
}

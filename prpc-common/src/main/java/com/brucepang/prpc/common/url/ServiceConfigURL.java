package com.brucepang.prpc.common.url;

import com.brucepang.prpc.common.URL;

import java.util.Map;

/**
 * @author BrucePang
 */
public class ServiceConfigURL extends URL {


    public ServiceConfigURL(URLAddress urlAddress, URLParam urlParam, Map<String, Object> attributes) {
        super(urlAddress, urlParam, attributes);
    }

    public ServiceConfigURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters) {
        this(new PathURLAddress(protocol, username, password, path, host, port), URLParam.parse(parameters), null);
    }

    public ServiceConfigURL(String protocol, String host, int port, String path, Map<String, String> params) {
        this(protocol, null, null, host, port, path, params);
    }
}

package com.brucepang.prpc.common;

import com.brucepang.prpc.common.url.PathURLAddress;
import com.brucepang.prpc.common.url.ServiceConfigURL;
import com.brucepang.prpc.common.url.URLAddress;
import com.brucepang.prpc.common.url.URLParam;
import com.brucepang.prpc.util.StrUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
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

    private final URLParam urlParam;

    private final URLAddress urlAddress;

    protected volatile Map<String, Object> attributes;

    protected URL(URLAddress urlAddress) {
        this.urlAddress = urlAddress;
        this.protocol = null;
        this.host = null;
        this.port = 0;
        this.path = null;
        this.urlParam = URLParam.parse(new HashMap<>());
    }

    public URL(String protocol, String host, int port, URLParam urlParam, URLAddress urlAddress) {
        this(protocol, host, port, null, null, null, urlParam, urlAddress);
    }

    public URL(String protocol, String host, int port, String path, URLParam urlParam, URLAddress urlAddress) {
        this(protocol, host, port, path, null, null, urlParam, urlAddress);
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> params, Map<String, Map<String, String>> methodParameters, URLParam urlParam, URLAddress urlAddress) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.urlParam = urlParam;
        this.urlAddress = urlAddress;
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> params, URLParam urlParam, URLAddress urlAddress) {
        this(protocol, host, port, path, params, null, urlParam, urlAddress);
    }

    public URL(URLAddress urlAddress, URLParam urlParam, Map<String, Object> attributes) {
        this.urlAddress = urlAddress;
        this.urlParam = null == urlParam ? URLParam.parse(new HashMap<>()) : urlParam;

        if (attributes != null && !attributes.isEmpty()) {
            this.attributes = attributes;
        } else {
            this.attributes = null;
        }
    }

    public URL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters) {
        if (StrUtil.isEmpty(username) && StrUtil.isNotEmpty(password)) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }

        this.urlAddress = new PathURLAddress(protocol, username, password, path, host, port);
        this.urlParam = URLParam.parse(parameters);
        this.attributes = null;
    }

    public URL(URLAddress urlAddress, URLParam urlParam) {
        this(urlAddress, urlParam, null);
    }


    /**
     * encode the URL encoded string using UTF-8.
     * @param value URL encoded string
     * @return  URL encoded string
     */
    public static String encode(String value) {
        if (StrUtil.isEmpty(value)) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Decode the URL encoded string using UTF-8.
     *
     * @param value URL encoded string
     * @return URL decoded string
     */
    public static String decode(String value) {
        if (StrUtil.isEmpty(value)) {
            return "";
        }
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public URL setProtocol(String protocol){
        return new ServiceConfigURL(protocol,host,port,path,getParams());
    }


    public Map<String, String> getParams() {
        return urlParam.getParameters();
    }

    public String getParameter(String key) {
        return urlParam.getParameter(key);
    }

    public static void putMethodParameter(
            String method, String key, String value, Map<String, Map<String, String>> methodParameters) {
        Map<String, String> subParameter = methodParameters.computeIfAbsent(method, k -> new HashMap<>());
        subParameter.put(key, value);
    }

}

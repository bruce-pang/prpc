package com.brucepang.prpc.util;

/**
 * @author BrucePang
 */
public class UrlUtils {
    private UrlUtils() {
    }

    public static String getHost(String url) {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        int index = url.indexOf("://");
        if (index == -1) {
            return null;
        }
        String host = url.substring(index + 3);
        index = host.indexOf("/");
        if (index != -1) {
            host = host.substring(0, index);
        }
        return host;
    }
}

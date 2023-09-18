package com.brucepang.prpc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP地址处理工具类
 * @author BrucePang
 */
public class IPAddressConverter {
    public static String convertToLocalhostIfNecessary(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return "127.0.0.1";
        }

        try {
            InetAddress localAddress = InetAddress.getByName("localhost");
            InetAddress ipv4Localhost = InetAddress.getByName("127.0.0.1");

            InetAddress targetAddress = InetAddress.getByName(ipAddress);

            if (targetAddress.isLoopbackAddress() || targetAddress.equals(localAddress)) {
                return "127.0.0.1";
            } else {
                return ipAddress;
            }
        } catch (UnknownHostException e) {
            // 处理异常，例如返回默认值或记录日志
            e.printStackTrace();
            return ipAddress;
        }
    }
}

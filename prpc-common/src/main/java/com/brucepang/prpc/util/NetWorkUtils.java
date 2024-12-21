package com.brucepang.prpc.util;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * a network util for IP address, port, etc.
 * @author BrucePang
 */
public final class NetWorkUtils {

    private final Logger logger = LoggerFactory.getLogger(NetWorkUtils.class);

        private NetWorkUtils() {
        }

    /**
     * @param hostName
     * @return ip address or hostName if UnknownHostException
     */
    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }
}

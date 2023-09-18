package com.brucepang.prpc.config;

/**
 * 注册中心默认的配置
 * @author BrucePang
 */
public class RegistryDefaultConfig {

    // DEFAULT HOST
    public static final String DEFAULT_HOST = "127.0.0.1";

    // NACOS
    public static final String NACOS = "nacos://";
    public static final int NACOS_PORT = 8848;

    // ZOOKEEPER
    public static final String ZOOKEEPER = "zookeeper://";
    public static final int ZK_PORT = 2181;

    // EUREKA
    public static final String EUREKA = "eureka://";
    public static final int EUREKA_PORT = 8761;

}

package com.brucepang.prpc.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author BrucePang
 */
@Data
@ConfigurationProperties(prefix = "com.brucepang.prpc.server")
public class PrpcServerProperties {
    private String serviceAddress; //注册中心地址
    private int servicePort; // 注册中心端口
    private byte registryType; // 注册中心的类型
    private String registryAddress; // 注册中心的地址

}

package com.brucepang.prpc.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author BrucePang
 */

@ConfigurationProperties(prefix = "com.brucepang.prpc.server") // 声明当前类是一个Properties类，定义在全局配置文件中的前缀
public class PrpcServerProperties {
    private String serviceAddress; //本地调用服务提供方地址
    private int servicePort; // 本地调用服务端口
    private byte registryType; // 注册中心的类型
    private String registryAddress; // 注册中心的地址

    // 启用注册中心的开关
    private boolean enableRegistry;

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public byte getRegistryType() {
        return registryType;
    }

    public void setRegistryType(byte registryType) {
        this.registryType = registryType;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }
}

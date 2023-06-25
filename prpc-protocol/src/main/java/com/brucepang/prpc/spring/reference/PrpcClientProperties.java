package com.brucepang.prpc.spring.reference;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author BrucePang
 */

public class PrpcClientProperties {
    // 本地调用使用
    private String serviceAddress;
    private int servicePort;

    // 注册中心使用
    private byte registryType;
    private String registryAddress;

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

    public boolean isEnableRegistry() {
        return enableRegistry;
    }

    public void setEnableRegistry(boolean enableRegistry) {
        this.enableRegistry = enableRegistry;
    }
}

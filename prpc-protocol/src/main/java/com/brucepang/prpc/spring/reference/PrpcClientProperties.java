package com.brucepang.prpc.spring.reference;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author BrucePang
 */
@Data
public class PrpcClientProperties {
    // 本地调用使用
    private String serviceAddress;
    private int servicePort;

    // 注册中心使用
    private byte registryType;
    private String registryAddress;

    // 启用注册中心的开关
    private boolean enableRegistry;

}

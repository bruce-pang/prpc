package com.brucepang.prpc.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author BrucePang
 */
@Data
@ConfigurationProperties(prefix = "com.brucepang.prpc.server")
public class PrpcServerProperties {
    private String serviceAddress;
    private String servicePort;

}

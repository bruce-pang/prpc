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
    private String serviceAddress;
    private int servicePort;

 /*   private byte registryType;
    private String registryAddress;*/


}

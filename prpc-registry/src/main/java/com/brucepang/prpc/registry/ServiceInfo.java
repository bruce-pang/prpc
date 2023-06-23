package com.brucepang.prpc.registry;

import lombok.Data;

/**
 * @author BrucePang
 */
@Data
public class ServiceInfo {
    private String serviceName;
    private String serviceAddress;
    private int servicePort;
}

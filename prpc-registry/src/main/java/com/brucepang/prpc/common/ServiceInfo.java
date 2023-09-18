package com.brucepang.prpc.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务提供者对外暴露的信息
 * @author BrucePang
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ServiceInfo {
    private String serviceName; // 服务名称
    private String serviceAddress; // ip地址
    private int servicePort; // 端口号
}

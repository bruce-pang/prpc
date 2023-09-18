package com.brucepang.prpc.common;

/**
 * @author BrucePang
 */
public interface IRegistryService {

    /**
     * 服务注册功能
     * @param serviceInfo
     */
    void register(ServiceInfo serviceInfo) throws Exception;



    /**
     * 服务发现功能
     * @param serviceName
     */
    ServiceInfo discovery(String serviceName) throws Exception;
}

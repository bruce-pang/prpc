package com.brucepang.prpc.rpc.model;

import com.brucepang.prpc.common.rpc.model.ServiceModel;

/**
 * @author BrucePang
 */
public class ServiceMetadata {

    private String serviceKey;
    private String serviceInterfaceName;
    private String version;
    private volatile String group;
    private ServiceModel serviceModel;

    public ServiceMetadata(String serviceInterfaceName, String group, String version) {
        this.serviceInterfaceName = serviceInterfaceName;
        this.group = group;
        this.version = version;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setServiceInterfaceName(String serviceInterfaceName) {
        this.serviceInterfaceName = serviceInterfaceName;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

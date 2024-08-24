package com.brucepang.prpc.rpc.protocol;

import com.brucepang.prpc.rpc.Invocation;

import java.io.Serializable;

/**
 * Rpc Invocation
 * @author BrucePang
 */
public class RpcInvocation implements Invocation, Serializable {

    private static final long serialVersionUID = -7782112276890205278L;

    private String methodName;

    private String serviceName;

    private Class<?>[] parameterTypes;

    public RpcInvocation() {
    }

    public RpcInvocation(String methodName, String serviceName, Class<?>[] parameterTypes) {
        this.methodName = methodName;
        this.serviceName = serviceName;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
}

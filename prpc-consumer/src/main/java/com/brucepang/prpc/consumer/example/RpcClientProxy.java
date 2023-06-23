package com.brucepang.prpc.consumer.example;

import java.lang.reflect.Proxy;

/**
 * @author BrucePang
 */
public class RpcClientProxy {
    public <T> T clientProxy(final Class<T> interfaceCls, final String host,  int port) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls},
                new RpcInvokerProxy(host, port));
    }
}

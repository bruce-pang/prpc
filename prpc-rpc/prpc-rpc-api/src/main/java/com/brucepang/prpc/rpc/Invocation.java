package com.brucepang.prpc.rpc;

/**
 * Invocation (API)
 * @author BrucePang
 */
public interface Invocation {
    /**
     * get method name.
     *
     * @return method name.
     * @serial
     */
    String getMethodName();

    /**
     * get the interface name
     *
     * @return
     */
    String getServiceName();

    /**
     * get parameter types.
     *
     * @return parameter types.
     * @serial
     */
    Class<?>[] getParameterTypes();
}

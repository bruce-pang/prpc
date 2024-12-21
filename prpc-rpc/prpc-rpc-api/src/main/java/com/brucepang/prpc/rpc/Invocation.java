package com.brucepang.prpc.rpc;

import java.util.stream.Stream;

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

    /**
     * get parameter's signature, string representation of parameter types.
     *
     * @return parameter's signature
     */
    default String[] getCompatibleParamSignatures() {
        return Stream.of(getParameterTypes()).map(Class::getName).toArray(String[]::new);
    }
}

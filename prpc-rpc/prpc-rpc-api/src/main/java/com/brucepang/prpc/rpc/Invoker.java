package com.brucepang.prpc.rpc;

import com.brucepang.prpc.common.Node;
import com.brucepang.prpc.exception.PrpcException;

public interface Invoker<T> extends Node {

    /**
     * get service interface.
     *
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     *
     * @param invocation
     * @return result
     * @throws PrpcException
     */
    Result invoke(Invocation invocation) throws PrpcException;



}
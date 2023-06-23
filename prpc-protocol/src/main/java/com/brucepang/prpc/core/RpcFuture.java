package com.brucepang.prpc.core;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author BrucePang
 */
@Data
public class RpcFuture<T> {
    private Promise<T> promise;

    public RpcFuture(Promise<T> promise) {
        this.promise = promise;
    }
}

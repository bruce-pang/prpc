package com.brucepang.prpc.extension.loader_strategy;

/**
 * @author BrucePang
 */
public class PrpcInternalLoadingStrategy implements LoadingStrategy{
    @Override
    public String directory() {
        return "META-INF/prpc/internal/";
    }

    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }

    @Override
    public String getName() {
        return "PRPC_INTERNAL";
    }

}

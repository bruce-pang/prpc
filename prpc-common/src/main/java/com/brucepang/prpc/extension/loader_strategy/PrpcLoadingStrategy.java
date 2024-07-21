package com.brucepang.prpc.extension.loader_strategy;

/**
 * Prpc loading extension point strategy
 * @author BrucePang
 */
public class PrpcLoadingStrategy implements LoadingStrategy{
    @Override
    public String directory() {
        return "META-INF/prpc/";
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    public String getName() {
        return "PRPC";
    }
}

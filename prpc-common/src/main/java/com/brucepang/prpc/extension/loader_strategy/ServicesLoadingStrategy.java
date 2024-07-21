package com.brucepang.prpc.extension.loader_strategy;

/**
 * JDK ServiceLoader loading extension point strategy
 * @author BrucePang
 */
public class ServicesLoadingStrategy implements LoadingStrategy {

    @Override
    public String directory() {
        return "META-INF/services/";
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return MIN_PRIORITY;
    }

    @Override
    public String getName() {
        return "SERVICES";
    }
}

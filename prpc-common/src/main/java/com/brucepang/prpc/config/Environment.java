package com.brucepang.prpc.config;

import com.brucepang.prpc.common.context.ApplicationExt;
import com.brucepang.prpc.common.context.LifecycleAdapter;

/**
 * @author BrucePang
 */
public class Environment extends LifecycleAdapter implements ApplicationExt {

    public static final String NAME = "environment";

    @Override
    public void initialize() throws IllegalStateException {

    }

    @Override
    public void start() throws IllegalStateException {

    }

    @Override
    public void destroy() throws IllegalStateException {

    }

    public Configuration getEnvironmentConfiguration() {
        return null;
    }
}

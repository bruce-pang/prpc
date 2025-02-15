package com.brucepang.prpc.config;

import com.brucepang.prpc.common.context.ApplicationExt;
import com.brucepang.prpc.common.context.LifecycleAdapter;
import com.brucepang.prpc.scope.model.ScopeModel;

/**
 * @author BrucePang
 */
public class Environment extends LifecycleAdapter implements ApplicationExt {

    public static final String NAME = "environment";

    private final ScopeModel scopeModel;

    // java system environment
    private EnvironmentConfiguration environmentConfiguration;

    public Environment(ScopeModel scopeModel) {
        this.scopeModel = scopeModel;
    }

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

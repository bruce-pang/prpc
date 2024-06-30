package com.brucepang.prpc.beans.strategy;

import com.brucepang.prpc.scope.model.ScopeModelAccessor;

/**
 * @author BrucePang
 */
public class InstantiationStrategy {
    private final ScopeModelAccessor scopeModelAccessor;

    public InstantiationStrategy() {
        this(null);
    }

    public InstantiationStrategy(ScopeModelAccessor scopeModelAccessor) {
        this.scopeModelAccessor = scopeModelAccessor;
    }
}

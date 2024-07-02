package com.brucepang.prpc.beans.strategy;

import com.brucepang.prpc.scope.model.ScopeModelAccessor;

import java.lang.reflect.Constructor;

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

    public <T> T instantiate(Class<T> type) throws ReflectiveOperationException {
        Constructor<?> targetConstructor = type.getConstructor();
        // todo: add more logic here
        return (T) targetConstructor.newInstance();
    }
}

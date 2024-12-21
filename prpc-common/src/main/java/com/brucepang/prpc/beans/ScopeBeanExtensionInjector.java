package com.brucepang.prpc.beans;

import com.brucepang.prpc.beans.factory.ScopeBeanFactory;
import com.brucepang.prpc.extension.ExtensionInjector;
import com.brucepang.prpc.extension.inject.ScopeModelAware;
import com.brucepang.prpc.scope.model.ScopeModel;

/**
 * Inject scope bean to SPI extension instance
 */
public class ScopeBeanExtensionInjector implements ExtensionInjector, ScopeModelAware {
    
    private ScopeBeanFactory beanFactory;

    @Override
    public void setScopeModel(final ScopeModel scopeModel) {
        this.beanFactory = scopeModel.getBeanFactory();
    }

    @Override
    public <T> T getInstance(final Class<T> type, final String name) {
        return beanFactory == null ? null : beanFactory.getBean(name, type);
    }
}

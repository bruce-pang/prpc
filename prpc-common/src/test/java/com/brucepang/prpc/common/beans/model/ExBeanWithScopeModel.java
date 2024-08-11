package com.brucepang.prpc.common.beans.model;

import com.brucepang.prpc.scope.model.ScopeModel;

/**
 * Bean with scope model argument.
 */
public class ExBeanWithScopeModel {
    private ScopeModel scopeModel;

    public ExBeanWithScopeModel(ScopeModel scopeModel) {
        this.scopeModel = scopeModel;
    }

    public ScopeModel getScopeModel() {
        return scopeModel;
    }
}

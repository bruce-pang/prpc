package com.brucepang.prpc.scope.model;

/**
 *  An accessor for scope model, it can be use in interface default methods to get scope model.
 *
 */
public interface ScopeModelAccessor {
    ScopeModel getScopeModel();

    default GlobalModel getGlobalModel() {
        return ScopeModelUtil.getGlobalModel(getScopeModel());
    }

    default ApplicationModel getApplicationModel() {
        return ScopeModelUtil.getApplicationModel(getScopeModel());
    }

    default ModuleModel getModuleModel() {
        return ScopeModelUtil.getModuleModel(getScopeModel());
    }
}

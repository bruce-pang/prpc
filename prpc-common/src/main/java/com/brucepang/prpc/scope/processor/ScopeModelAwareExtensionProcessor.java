package com.brucepang.prpc.scope.processor;

import com.brucepang.prpc.extension.ExtensionPostProcessor;
import com.brucepang.prpc.extension.inject.ScopeModelAware;
import com.brucepang.prpc.scope.model.*;

/**
 *
 * @author BrucePang
 */
public class ScopeModelAwareExtensionProcessor implements ExtensionPostProcessor, ScopeModelAccessor {
    private ScopeModel scopeModel;
    private GlobalModel globalModel;
    private ApplicationModel applicationModel;
    private ModuleModel moduleModel;

    public ScopeModelAwareExtensionProcessor(ScopeModel scopeModel) {
        this.scopeModel = scopeModel;
        initialize();
    }

    private void initialize() {
        if (scopeModel instanceof GlobalModel) {
            globalModel = (GlobalModel) scopeModel;
        } else if (scopeModel instanceof ApplicationModel) {
            applicationModel = (ApplicationModel) scopeModel;
            globalModel = applicationModel.getGlobalModel();
        } else if (scopeModel instanceof ModuleModel) {
            moduleModel = (ModuleModel) scopeModel;
            applicationModel = moduleModel.getApplicationModel();
            globalModel = applicationModel.getGlobalModel();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object instance, String name) throws Exception {
        return ExtensionPostProcessor.super.postProcessBeforeInitialization(instance, name);
    }

    @Override
    public Object postProcessAfterInitialization(Object instance, String name) throws Exception {
        if (instance instanceof ScopeModelAware){
            ScopeModelAware modelAware = (ScopeModelAware) instance;
            modelAware.setScopeModel(scopeModel);
            if (this.moduleModel != null) {
                modelAware.setModuleModel(this.moduleModel);
            }
            if (this.applicationModel != null) {
                modelAware.setApplicationModel(this.applicationModel);
            }
            if (this.globalModel != null) {
                modelAware.setGlobalModel(this.globalModel);
            }
        }
        return instance;
    }

    @Override
    public ScopeModel getScopeModel() {
        return scopeModel;
    }

    @Override
    public GlobalModel getGlobalModel() {
        return globalModel;
    }

    @Override
    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }

    @Override
    public ModuleModel getModuleModel() {
        return moduleModel;
    }
}

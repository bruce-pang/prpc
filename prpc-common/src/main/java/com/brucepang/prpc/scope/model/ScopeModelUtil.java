package com.brucepang.prpc.scope.model;


import com.brucepang.prpc.extension.ExtensionLoader;
import com.brucepang.prpc.extension.SPI;


public class ScopeModelUtil {

    public static <T> ScopeModel getScopeModel(ScopeModel scopeModel, Class<T> type) {
        return scopeModel != null ? scopeModel : getDefaultScopeModel(type);
    }

    private static <T> ScopeModel getDefaultScopeModel(Class<T> type) {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi == null) {
            throw new IllegalArgumentException("SPI annotation not found for class: " + type.getName());
        }
        switch (spi.scope()) {
            case GLOBAL:
                return GlobalModel.defaultModel();
            case APPLICATION:
                return ApplicationModel.defaultModel();
            case MODULE:
                return ApplicationModel.defaultModel().getDefaultModule();
            default:
                throw new IllegalStateException("Unable to get default scope model for type: " + type.getName());
        }
    }

    public static ModuleModel getModuleModel(ScopeModel scopeModel) {
        if (scopeModel instanceof ModuleModel) {
            return (ModuleModel) scopeModel;
        } else {
            throw new IllegalArgumentException("Unable to get ModuleModel from " + scopeModel);
        }
    }

    public static ApplicationModel getApplicationModel(ScopeModel scopeModel) {
        if (scopeModel instanceof ApplicationModel) {
            return (ApplicationModel) scopeModel;
        } else if (scopeModel instanceof ModuleModel) {
            return ((ModuleModel) scopeModel).getApplicationModel();
        } else {
            throw new IllegalArgumentException("Unable to get ApplicationModel from " + scopeModel);
        }
    }

    public static GlobalModel getFrameworkModel(ScopeModel scopeModel) {
        if (scopeModel instanceof GlobalModel) {
            return (GlobalModel) scopeModel;
        } else if (scopeModel instanceof ApplicationModel) {
            return ((ApplicationModel) scopeModel).getGlobalModel();
        } else if (scopeModel instanceof ModuleModel) {
            return ((ModuleModel) scopeModel).getApplicationModel().getGlobalModel();
        } else {
            throw new IllegalArgumentException("Unable to get GlobalModel from " + scopeModel);
        }
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type, ScopeModel scopeModel) {
        if (scopeModel != null) {
            return scopeModel.getExtensionLoader(type);
        } else {
            return getDefaultScopeModel(type).getExtensionLoader(type);
        }
    }
}
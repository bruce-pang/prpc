package com.brucepang.prpc.extension.inject;

import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.GlobalModel;
import com.brucepang.prpc.scope.model.ModuleModel;
import com.brucepang.prpc.scope.model.ScopeModel;

/**
 * An interface to inject GlobalModule/ApplicationModel/ModuleModel for SPI extensions and internal beans.
 * @author BrucePang
 */
public interface ScopeModelAware {
    /**
     * Override this method if you need get the scope model (maybe one of GlobalModule/ApplicationModel/ModuleModel).
     * @param scopeModel the ScopeModel
     */
    default void setScopeModel(ScopeModel scopeModel) {
    }

    /**
     * Override this method if you just need GlobalModel
     * @param globalModel the GlobalModel
     */
    default void setGlobalModel(GlobalModel globalModel) {
    }

    /**
     * Override this method if you just need application model
     * @param applicationModel the ApplicationModel
     */
    default void setApplicationModel(ApplicationModel applicationModel) {
    }

    /**
     * Override this method if you just need module model
     * @param moduleModel the ModuleModel
     */
    default void setModuleModel(ModuleModel moduleModel) {
    }
}

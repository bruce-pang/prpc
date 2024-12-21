package com.brucepang.prpc.common.beans.model;

import com.brucepang.prpc.scope.model.ModuleModel;

/**
 * A bean with {@link ModuleModel} argument.
 * @author BrucePang
 */
public class ExBeanWithModuleModel {
    private ModuleModel moduleModel;

    public ExBeanWithModuleModel(ModuleModel moduleModel) {
        this.moduleModel = moduleModel;
    }

    public ModuleModel getModuleModel() {
        return moduleModel;
    }


}

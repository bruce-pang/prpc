package com.brucepang.prpc.common.beans.model;

import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.ModuleModel;

/**
 * @author BrucePang
 */
public class ExBeanWithoutUniqueConstructors {
    private ModuleModel moduleModel;
    private ApplicationModel applicationModel;

    public ExBeanWithoutUniqueConstructors(ModuleModel moduleModel) {
        this.moduleModel = moduleModel;
    }

    public ExBeanWithoutUniqueConstructors(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }
}

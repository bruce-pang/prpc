package com.brucepang.prpc.config;

import com.brucepang.prpc.scope.model.ModuleModel;

/**
 * @author BrucePang
 */
public class ModuleEnvironment extends Environment {
    public static final String NAME = "moduleEnvironment";

    private final ModuleModel moduleModel;

    public ModuleEnvironment(ModuleModel moduleModel) {
        super(moduleModel);
        this.moduleModel = moduleModel;
    }

}

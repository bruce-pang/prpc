package com.brucepang.prpc.config;

import com.brucepang.prpc.scope.model.ModuleModel;

import java.io.Serial;

/**
 * @author BrucePang
 */
public abstract class AbstractMethodConfig  extends AbstractConfig{
    @Serial
    private static final long serialVersionUID = 6122396736656165438L;

    public AbstractMethodConfig() {}

    public AbstractMethodConfig(ModuleModel moduleModel) {
        super(moduleModel);
    }
}

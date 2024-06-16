package com.brucepang.prpc.scope;

import com.brucepang.prpc.extension.ExtensionMgt;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.util.Assert;

/**
 * Model of a service module
 *
 * @author BrucePang
 */
public class ModuleModel extends ScopeModel{
    private static final Logger logger = LoggerFactory.getLogger(ModuleModel.class);

    public static final String NAME = "ModuleModel";

    private final ApplicationModel applicationModel;


    public ModuleModel(ApplicationModel applicationModel) {
        super(applicationModel, ExtensionScope.MODULE);
        Assert.notNull(applicationModel, "ApplicationModel can not be null");
        this.applicationModel = applicationModel;

        initialize();
    }


    @Override
    public ExtensionMgt getExtensionMgt() {
        return null;
    }
}

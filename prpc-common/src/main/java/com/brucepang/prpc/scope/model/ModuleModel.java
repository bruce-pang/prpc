package com.brucepang.prpc.scope.model;

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


    protected ModuleModel(ApplicationModel applicationModel) {
        this(applicationModel, false);
    }

    public ModuleModel(ApplicationModel applicationModel, boolean isInternal) {
        super(applicationModel, ExtensionScope.MODULE, isInternal);
        Assert.notNull(applicationModel, "ApplicationModel can not be null");
        this.applicationModel = applicationModel;

        initialize();
    }

    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }

    @Override
    public ExtensionMgt getExtensionMgt() {
        return null;
    }
}

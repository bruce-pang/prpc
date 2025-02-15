package com.brucepang.prpc.scope.model;

import com.brucepang.prpc.config.Environment;
import com.brucepang.prpc.config.ModuleEnvironment;
import com.brucepang.prpc.context.ModuleExt;
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

    private volatile ModuleEnvironment moduleEnvironment;


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

    @Override
    public Environment modelEnvironment() {
        if (moduleEnvironment == null) {
            moduleEnvironment =
                    (ModuleEnvironment) this.getExtensionLoader(ModuleExt.class).getExtension(ModuleEnvironment.NAME);
        }
        return moduleEnvironment;
    }
}

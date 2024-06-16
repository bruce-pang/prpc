package com.brucepang.prpc.scope;

import com.brucepang.prpc.extension.ExtensionLoader;
import com.brucepang.prpc.extension.ExtensionMgt;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApplicationModel extends ScopeModel {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ApplicationModel.class);
    public static final String NAME = "ApplicationModel";
    private volatile ModuleModel defaultModule;
    private final List<ModuleModel> moduleModels = new CopyOnWriteArrayList<>();
    private Object moduleLock = new Object();
    private ModuleModel internalModule;

    protected ApplicationModel(ScopeModel parent, ExtensionScope scope) {
        super(parent, scope);
    }

    public ApplicationModel(GlobalModule globalModule) {
        this(globalModule, ExtensionScope.APPLICATION);
        internalModule = new ModuleModel(this);
    }

    public static ApplicationModel defaultModel() {
        // should get from default FrameworkModel, avoid out of sync
        return GlobalModule.defaultModel().defaultApplication();
    }

    public ModuleModel newModule() {
        return new ModuleModel(this);
    }


    @Override
    public ExtensionMgt getExtensionMgt() {
        return null;
    }

    @Override
    public <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return super.getExtensionLoader(type);
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        return super.getExtension(type, name);
    }

    public ModuleModel getDefaultModule() {
        if (defaultModule == null) {
            synchronized (moduleLock) {
                if (defaultModule == null) {
                    defaultModule = findDefaultModule();
                    if (defaultModule == null) {
                        defaultModule = this.newModule();
                    }
                }
            }
        }
        return defaultModule;
    }


    private ModuleModel findDefaultModule() {
        for (ModuleModel moduleModel : moduleModels) {
            if (moduleModel != internalModule) {
                return moduleModel;
            }
        }
        return null;
    }
}
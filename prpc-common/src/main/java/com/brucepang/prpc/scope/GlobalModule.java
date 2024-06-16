package com.brucepang.prpc.scope;

import com.brucepang.prpc.extension.ExtensionMgt;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.util.Assert;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalModule extends ScopeModel {
    private static final Object globalLock = new Object();
    private volatile static GlobalModule defaultInstance;
    private static List<GlobalModule> allInstances = new CopyOnWriteArrayList<>();
    private final AtomicBoolean destroyed = new AtomicBoolean(false);
    private volatile ApplicationModel defaultAppModel;
    private final Object instLock = new Object();

    public GlobalModule() {
        super(null, ExtensionScope.GLOBAL);
        synchronized (globalLock) {
            allInstances.add(this);
            resetDefaultGlobalModule();
        }
    }

    public ApplicationModel newApplication() {
        return new ApplicationModel(this);
    }

    private static void resetDefaultGlobalModule() {
        synchronized (globalLock) {
            if (defaultInstance != null && !defaultInstance.isDestroyed()) {
                return;
            }
            GlobalModule oldDefaultFrameworkModel = defaultInstance;
            if (allInstances.size() > 0) {
                defaultInstance = allInstances.get(0);
            } else {
                defaultInstance = null;
            }
        }
    }

    public static GlobalModule defaultModel() {
        GlobalModule instance = defaultInstance;
        if (instance == null) {
            synchronized (globalLock) {
                resetDefaultFrameworkModel();
                if (defaultInstance == null) {
                    defaultInstance = new GlobalModule();
                }
                instance = defaultInstance;
            }
        }
        Assert.notNull(instance, "Default FrameworkModel is null");
        return instance;
    }

    private static void resetDefaultFrameworkModel() {
        synchronized (globalLock) {
            if (defaultInstance != null && !defaultInstance.isDestroyed()) {
                return;
            }
            if (allInstances.size() > 0) {
                defaultInstance = allInstances.get(0);
            } else {
                defaultInstance = null;
            }
        }
    }

    public boolean isDestroyed() {
        return destroyed.get();
    }

    @Override
    public ExtensionMgt getExtensionMgt() {
        return null;
    }

    public ApplicationModel defaultApplication() {
        ApplicationModel appModel = this.defaultAppModel;
        if (appModel == null) {
            // check destroyed before acquire inst lock, avoid blocking during destroying
            if ((appModel = this.defaultAppModel) == null) {
                synchronized (instLock) {
                    if (this.defaultAppModel == null) {
                        this.defaultAppModel = newApplication();
                    }
                    appModel = this.defaultAppModel;
                }
            }
        }
        Assert.notNull(appModel, "Default ApplicationModel is null");
        return appModel;
    }
}
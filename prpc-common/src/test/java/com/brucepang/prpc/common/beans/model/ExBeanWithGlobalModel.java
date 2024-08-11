package com.brucepang.prpc.common.beans.model;

import com.brucepang.prpc.common.lifecycle.Disposable;
import com.brucepang.prpc.scope.model.GlobalModel;

/**
 * A bean with {@link GlobalModel} argument.
 * @author BrucePang
 */
public class ExBeanWithGlobalModel implements Disposable {
    private GlobalModel globalModel;

    private boolean destroyed;

    public ExBeanWithGlobalModel(GlobalModel globalModel) {
        this.globalModel = globalModel;
    }

    public GlobalModel getGlobalModel() {
        return globalModel;
    }

    @Override
    public void destroy() {
        this.destroyed = true;
    }
}

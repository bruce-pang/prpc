package com.brucepang.prpc.common.beans.model;

import com.brucepang.prpc.common.lifecycle.Disposable;
import com.brucepang.prpc.scope.model.ApplicationModel;

/**
 * A bean with {@link ApplicationModel} argument.
 * @author BrucePang
 */
public class ExBeanWithApplicationModel implements Disposable {
    private ApplicationModel applicationModel;

    private boolean destroyed;

    public ExBeanWithApplicationModel(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }

    @Override
    public void destroy() {
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}

package com.brucepang.prpc.scope.model;

import com.brucepang.prpc.beans.factory.ScopeBeanFactory;
import com.brucepang.prpc.extension.ExtensionAccessor;
import com.brucepang.prpc.extension.ExtensionMgt;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.util.Set;

/**
 * @author BrucePang
 */
public abstract class ScopeModel implements ExtensionAccessor {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ScopeModel.class);
    /**
     * Public Model Name, can be set from user
     */
    private String modelName;

    private String desc;

    private Set<ClassLoader> classLoaders;

    private final ScopeModel parent;
    private final ExtensionScope scope;

    private ExtensionMgt extensionMgt;

    private volatile ScopeBeanFactory beanFactory;

    private final Object lock = new Object();

    private final boolean internalScope;

    protected ScopeModel(ScopeModel parent, ExtensionScope scope, boolean isInternal) {
        this.parent = parent;
        this.scope = scope;
        this.internalScope = isInternal;
    }


    protected void initialize() {
        // prepare to build Parent Delegation Mechanism
        synchronized (lock) {
            this.extensionMgt = new ExtensionMgt(scope, parent != null ? parent.getExtensionMgt() : null, this);
            ClassLoader classLoader = ScopeModel.class.getClassLoader();
            if(classLoaders != null) {
                this.addClassLoader(classLoader);
            }
        }
    }

    public void addClassLoader(ClassLoader classLoader) {
        synchronized (lock) {
           this.classLoaders.add(classLoader);
           if (parent != null) {
               parent.addClassLoader(classLoader);
           }
        }
    }

    public void destroy(){
        classLoaders.clear();
    }

    public ExtensionMgt getExtensionMgt() {
        return extensionMgt;
    }

}

package com.brucepang.prpc.scope;

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

    protected ScopeModel(ScopeModel parent, ExtensionScope scope) {
        this.parent = parent;
        this.scope = scope;
    }

    protected void initialize() {
        this.extensionMgt = new ExtensionMgt(scope);
    }
}

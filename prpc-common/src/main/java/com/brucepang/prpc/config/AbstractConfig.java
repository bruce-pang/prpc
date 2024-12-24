package com.brucepang.prpc.config;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.scope.model.ScopeModel;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author BrucePang
 */
public abstract class AbstractConfig implements Serializable {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);

    @Serial
    private static final long serialVersionUID = 6122104176448570797L;

    /**
     * The scope model of this config instance.
     * <p>
     * <b>NOTE:</b> the model maybe changed during config processing,
     * the extension spi instance needs to be reinitialized after changing the model!
     */
    private transient volatile ScopeModel scopeModel;

    public AbstractConfig() {
        this(null);
    }

    public AbstractConfig(ScopeModel scopeModel) {
        this.setScopeModel(scopeModel);
    }

    /**
     * tag name cache, speed up get tag name frequently
     */
    private static final ConcurrentMap<Class, String> tagNameCache = new ConcurrentHashMap<>();

    /**
     * attributed getter method cache for equals(), hashCode() and toString()
     */
    private static final ConcurrentMap<Class, List<Method>> attributedMethodCache = new ConcurrentHashMap<>();

    /**
     * The suffix container
     */
    private static final String[] SUFFIXES = new String[] {"Config", "Bean", "ConfigBase"};

    /**
     * The config id
     */
    private String id;

    protected final AtomicBoolean refreshed = new AtomicBoolean(false);


    public ScopeModel getScopeModel() {
        return scopeModel;
    }

    public void setScopeModel(ScopeModel scopeModel) {
        this.scopeModel = scopeModel;
    }
}

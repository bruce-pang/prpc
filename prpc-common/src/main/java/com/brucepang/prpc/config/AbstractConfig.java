package com.brucepang.prpc.config;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author BrucePang
 */
public abstract class AbstractConfig implements Serializable {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);

    @Serial
    private static final long serialVersionUID = 6122104176448570797L;


}

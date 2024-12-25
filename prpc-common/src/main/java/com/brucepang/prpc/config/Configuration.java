package com.brucepang.prpc.config;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

/**
 * Configuration interface, to fetch the value for the specified key.
 * @author BrucePang
 */
public interface Configuration {
    Logger logger = LoggerFactory.getLogger(Configuration.class);

    Object getInternalProperty(String key);
}

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

    /**
     * Gets a property from the configuration. The default value will return if the configuration doesn't contain
     * the mapping for the specified key.
     *
     * @param key property to retrieve
     * @param defaultValue default value
     * @return the value to which this configuration maps the specified key, or default value if the configuration
     * contains no mapping for this key.
     */
    default Object getProperty(String key, Object defaultValue) {
        Object value = getInternalProperty(key);
        return value != null ? value : defaultValue;
    }
}

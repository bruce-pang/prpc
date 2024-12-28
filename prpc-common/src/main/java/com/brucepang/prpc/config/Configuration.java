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

    /**
     * Gets a property from the configuration. This is the most basic get
     * method for retrieving values of properties. In a typical implementation
     * of the {@code Configuration} interface the other get methods (that
     * return specific data types) will internally make use of this method. On
     * this level variable substitution is not yet performed. The returned
     * object is an internal representation of the property value for the passed
     * in key. It is owned by the {@code Configuration} object. So a caller
     * should not modify this object. It cannot be guaranteed that this object
     * will stay constant over time (i.e. further update operations on the
     * configuration may change its internal state).
     *
     * @param key property to retrieve
     * @return the value to which this configuration maps the specified key, or
     * null if the configuration contains no mapping for this key.
     */
    default Object getProperty(String key) {
        return getProperty(key, null);
    }
}

package com.brucepang.prpc.config;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.util.NoSuchElementException;

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

    default <T> T convert(Class<T> cls, String key, T defaultValue) {
        // we only process String properties for now
        String value = (String) getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        Object obj = value;
        if (cls.isInstance(value)) {
            return cls.cast(value);
        }

        if (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)) {
            obj = Boolean.valueOf(value);
        } else if (Number.class.isAssignableFrom(cls) || cls.isPrimitive()) {
            if (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) {
                obj = Integer.valueOf(value);
            } else if (Long.class.equals(cls) || Long.TYPE.equals(cls)) {
                obj = Long.valueOf(value);
            } else if (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) {
                obj = Byte.valueOf(value);
            } else if (Short.class.equals(cls) || Short.TYPE.equals(cls)) {
                obj = Short.valueOf(value);
            } else if (Float.class.equals(cls) || Float.TYPE.equals(cls)) {
                obj = Float.valueOf(value);
            } else if (Double.class.equals(cls) || Double.TYPE.equals(cls)) {
                obj = Double.valueOf(value);
            }
        } else if (cls.isEnum()) {
            obj = Enum.valueOf(cls.asSubclass(Enum.class), value);
        }

        return cls.cast(obj);
    }

    /**
     * Get a string associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated string.
     */
    default String getString(String key) {
        return convert(String.class, key, null);
    }

    /**
     * Get a string associated with the given configuration key.
     * If the key doesn't map to an existing object, the default value
     * is returned.
     *
     * @param key          The configuration key.
     * @param defaultValue The default value.
     * @return The associated string if key is found and has valid
     * format, default value otherwise.
     */
    default String getString(String key, String defaultValue) {
        return convert(String.class, key, defaultValue);
    }

    default Integer getInteger(String key, Integer defaultValue) {
        try {
            return convert(Integer.class, key, defaultValue);
        } catch (NumberFormatException e) {
            throw new IllegalStateException('\'' + key + "' doesn't map to a Integer object", e);
        }
    }

    default int getInt(String key) {
        Integer i = this.getInteger(key, null);
        if (i != null) {
            return i;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    default int getInt(String key, int defaultValue) {
        Integer i = this.getInteger(key, null);
        return i == null ? defaultValue : i;
    }

    default Boolean getBoolean(String key, Boolean defaultValue) {
        try {
            return convert(Boolean.class, key, defaultValue);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Try to get " + '\'' + key + "' failed, maybe because this key doesn't map to a Boolean object", e);
        }
    }

    static Boolean toBooleanObject(boolean bool) {
        return bool ? Boolean.TRUE : Boolean.FALSE;
    }

    default boolean getBoolean(String key, boolean defaultValue) {
        return this.getBoolean(key, toBooleanObject(defaultValue));
    }


    default boolean getBoolean(String key) {
        Boolean b = this.getBoolean(key, null);
        if (b != null) {
            return b;
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    default boolean containsKey(String key) {
        return !ConfigurationUtils.isEmptyValue(getProperty(key));
    }

}

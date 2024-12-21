package com.brucepang.prpc.config;

import com.brucepang.prpc.util.StrUtil;

import java.util.Map;

public class EnvironmentConfiguration implements Configuration {

    @Override
    public Object getInternalProperty(String key) {
        String value = getenv(key);
        if (StrUtil.isEmpty(value)) {
            value = getenv(StrUtil.toOSStyleKey(key));
        }
        return value;
    }

    public Map<String, String> getProperties() {
        return getenv();
    }

    // Adapt to System api, design for unit test

    protected String getenv(String key) {
        return System.getenv(key);
    }

    protected Map<String, String> getenv() {
        return System.getenv();
    }
}

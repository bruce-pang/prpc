package com.brucepang.prpc.common.url.bean.cache;

import com.brucepang.prpc.util.StrUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BrucePang
 */
public class URLItemCache {

    private static final Map<String, String> PARAM_KEY_CACHE = new LinkedHashMap<String, String>(16, 0.75f, true);
    private static final Map<String, String> PARAM_VALUE_CACHE = new LinkedHashMap<String, String>(16, 0.75f, true);

    public static void putParams(Map<String, String> params, String key, String value) {
        String cachedKey = PARAM_KEY_CACHE.get(key);
        if (StrUtil.isBlank(cachedKey)) {
            cachedKey = key;
            PARAM_KEY_CACHE.put(key, key);
        }
        String cachedValue = PARAM_VALUE_CACHE.get(value);
        if (StrUtil.isBlank(cachedValue)) {
            cachedValue = value;
            PARAM_VALUE_CACHE.put(value, value);
        }
        params.put(cachedKey, cachedValue);
    }

}

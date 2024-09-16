package com.brucepang.prpc.common.url;

import java.util.BitSet;
import java.util.Map;

/**
 * @author BrucePang
 */
public class URLParam {

    /**
     * using bit to save if index exist even if value is default value
     */
    private final BitSet KEY;

    /**
     * an array which contains value-offset
     */
    private final int[] VALUE;

    public URLParam() {
        KEY = null;
        VALUE = null;
    }

    public static URLParam parse(Map<String, String> params) {
        return parse(params, null);
    }

    private static URLParam parse(Map<String, String> params, String dynamicParam) {
        URLParam urlParam = new URLParam();
        if (params == null) {
            return urlParam;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                urlParam.put(key, value);
            }
        }
        return urlParam;

    }

    private void put(String key, String value) {
        int offset = 0;
        if (value == null) {
            offset = -1;
        } else {
            offset = 0;
        }
        KEY.set(offset);
        VALUE[offset] = offset;
    }

}

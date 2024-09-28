package com.brucepang.prpc.common.url.param;

import com.brucepang.prpc.scope.model.GlobalModel;

import java.util.*;

/**
 * Global Param Cache Table
 * Not support method parameters
 */
public final class DynamicParamTable {
    /**
     * Keys array, value is string
     */
    private static String[] ORIGIN_KEYS;

    private static ParamValue[] VALUES;
    private static final Map<String, Integer> KEY2INDEX = new HashMap<>(64);

    private DynamicParamTable() {
        throw new IllegalStateException();
    }

    static {
        init();
    }

    private static void init() {
        List<String> keys = new LinkedList<>();
        List<ParamValue> values = new LinkedList<>();
        Map<String, Integer> key2Index = new HashMap<>(64);
        keys.add("");
        values.add(new DynamicValues(null));

        GlobalModel.defaultModel()
                .getExtensionLoader(DynamicParamSource.class)
                .getSupportedExtensionInstances()
                .forEach(source -> source.init(keys, values));

        TreeMap<String, ParamValue> resultMap = new TreeMap<>(Comparator.comparingInt(System::identityHashCode));
        for (int i = 0; i < keys.size(); i++) {
            resultMap.put(keys.get(i), values.get(i));
        }

        ORIGIN_KEYS = resultMap.keySet().toArray(new String[0]);

        VALUES = resultMap.values().toArray(new ParamValue[0]);

        for (int i = 0; i < ORIGIN_KEYS.length; i++) {
            key2Index.put(ORIGIN_KEYS[i], i);
        }
        KEY2INDEX.putAll(key2Index);
    }

    public static String getKey(int offset) {
        return ORIGIN_KEYS[offset];
    }

    public static String getValue(int vi, int offset) {
        return VALUES[vi].getN(offset);
    }

    public static int getKeyIndex(boolean enableCompressed, String key) {
        if (!enableCompressed) {
            return -1;
        }
        Integer indexFromMap = KEY2INDEX.get(key);
        return indexFromMap == null ? -1 : indexFromMap;
    }

    public static int getValueIndex(String key, String value) {
        int idx = getKeyIndex(true, key);
        if (idx < 0) {
            throw new IllegalArgumentException("Cannot found key in url param:" + key);
        }
        ParamValue paramValue = VALUES[idx];
        return paramValue.getIndex(value);
    }


}

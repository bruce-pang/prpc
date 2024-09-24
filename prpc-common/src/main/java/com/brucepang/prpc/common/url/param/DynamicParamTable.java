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
}

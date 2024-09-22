package com.brucepang.prpc.common.url.param;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BrucePang
 */
public class DynamicValues implements ParamValue {

    private volatile String[] index2Value = new String[1];
    private final Map<String, Integer> value2Index = new ConcurrentHashMap<>();
    private int indexSeq = 0;

    @Override
    public int getIndex(String value) {
        if (value == null) {
            return -1;
        }
        Integer index = value2Index.get(value);
        if (index == null) {
            return add(value);
        }
        return index;
    }

    @Override
    public String getN(int n) {
        if (n == -1) {
            return null;
        }
        return index2Value[n];
    }

    public int add(String value) {
        Integer index = value2Index.get(value);
        if (index != null) {
            return index;
        } else {
            synchronized (this) {
                // thread safe
                if (!value2Index.containsKey(value)) {
                    if (indexSeq == Integer.MAX_VALUE) {
                        throw new IllegalStateException("URL Param Cache is full.");
                    }
                    // copy on write, only support append now
                    String[] newValues = new String[indexSeq + 1];
                    System.arraycopy(index2Value, 0, newValues, 0, indexSeq);
                    newValues[indexSeq] = value;
                    index2Value = newValues;
                    value2Index.put(value, indexSeq);
                    indexSeq += 1;
                }
            }
        }
        return value2Index.get(value);
    }

    public DynamicValues(String defaultVal) {
        if (defaultVal == null) {
            indexSeq += 1;
        } else {
            add(defaultVal);
        }
    }



}

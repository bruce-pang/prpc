package com.brucepang.prpc.common.url;

import java.util.BitSet;

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
}

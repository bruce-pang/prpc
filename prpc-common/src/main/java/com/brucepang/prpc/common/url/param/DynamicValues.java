package com.brucepang.prpc.common.url.param;

/**
 * @author BrucePang
 */
public class DynamicValues implements ParamValue {
    @Override
    public int getIndex(String value) {
        return 0;
    }

    @Override
    public String getN(int n) {
        return "";
    }
}

package com.brucepang.prpc.util;

public abstract class NativeUtils {

    public static boolean isNative() {
        return Boolean.parseBoolean(System.getProperty("native", "false"));
    }

}

package com.brucepang.prpc.util;

/**
 * string util
 * @author BrucePang
 */
public class StrUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        if (str != null && !str.isEmpty()) {
            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if (!Character.isWhitespace(c)) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

}

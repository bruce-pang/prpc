package com.brucepang.prpc.util;

import java.io.PrintWriter;
import java.io.StringWriter;

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
    /**
     * if name1 is null and name2 is null, then return true
     *
     * @param name1 name1
     * @param name2 name2
     * @return equals
     */
    public static boolean isEquals(String name1, String name2) {
        if (name1 == null && name2 == null) {
            return true;
        }
        if (name1 == null || name2 == null) {
            return false;
        }
        return name1.equals(name2);
    }

    /**
     * @param e
     * @return string
     */
    public static String toString(Throwable e) {
        if (e == null) {
            return null;
        }
        StringWriter w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        return w.toString();
    }
}

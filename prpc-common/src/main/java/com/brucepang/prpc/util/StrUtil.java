package com.brucepang.prpc.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.brucepang.prpc.common.constants.CommonConstants.DOT_REGEX;
import static com.brucepang.prpc.common.constants.CommonConstants.UNDERLINE_SEPARATOR;

/**
 * string util
 * @author BrucePang
 */
public final class StrUtil {

    private static final byte[] HEX2B = new byte[128];
    public static final String EMPTY_STRING = "";

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

    /**
     * Decode a 2-digit hex byte from within a string.
     */
    public static byte decodeHexByte(CharSequence s, int pos) {
        int hi = decodeHexNibble(s.charAt(pos));
        int lo = decodeHexNibble(s.charAt(pos + 1));
        if (hi == -1 || lo == -1) {
            throw new IllegalArgumentException(
                    String.format("invalid hex byte '%s' at index %d of '%s'", s.subSequence(pos, pos + 2), pos, s));
        }
        return (byte) ((hi << 4) + lo);
    }
    public static int decodeHexNibble(final char c) {
        // Character.digit() is not used here, as it addresses a larger
        // set of characters (both ASCII and full-width latin letters).
        byte[] hex2b = HEX2B;
        return c < hex2b.length ? hex2b[c] : -1;
    }

    public static String toOSStyleKey(String key) {
        key = key.toUpperCase().replaceAll(DOT_REGEX, UNDERLINE_SEPARATOR);
        if (!key.startsWith("PRPC_")) {
            key = "PRPC_" + key;
        }
        return key;
    }
}

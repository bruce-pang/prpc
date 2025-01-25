package com.brucepang.prpc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

import static com.brucepang.prpc.common.constants.CommonConstants.DOT_REGEX;
import static com.brucepang.prpc.common.constants.CommonConstants.UNDERLINE_SEPARATOR;
import static com.brucepang.prpc.common.constants.CommonConstants.SEPARATOR_REGEX;
import static com.brucepang.prpc.common.constants.CommonConstants.HIDE_KEY_PREFIX;

/**
 * string util
 * @author BrucePang
 */
public final class StrUtil {

    private static final byte[] HEX2B = new byte[128];
    public static final String EMPTY_STRING = "";

    public static final int INDEX_NOT_FOUND = -1;

    private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); // key value pair pattern.

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

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }


    /**
     * Test str whether starts with the prefix ignore case.
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null || str.length() < prefix.length()) {
            return false;
        }
        // return str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    protected static boolean isSnakeCase(String str) {
        return str.contains("_") || str.equals(str.toLowerCase()) || str.equals(str.toUpperCase());
    }

    public static String toURLKey(String key) {
        return key.toLowerCase().replaceAll(SEPARATOR_REGEX, HIDE_KEY_PREFIX);
    }

    public static boolean isNoneEmpty(final String... ss) {
        if (ArrayUtils.isEmpty(ss)) {
            return false;
        }
        for (final String s : ss) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the strings contain at least on empty or null element. <p/>
     *
     * <pre>
     * StrUtils.isAnyEmpty(null)            = true
     * StrUtils.isAnyEmpty("")              = true
     * StrUtils.isAnyEmpty(" ")             = false
     * StrUtils.isAnyEmpty("abc")           = false
     * StrUtils.isAnyEmpty("abc", "def")    = false
     * StrUtils.isAnyEmpty("abc", null)     = true
     * StrUtils.isAnyEmpty("abc", "")       = true
     * StrUtils.isAnyEmpty("abc", " ")      = false
     * </pre>
     *
     * @param ss the strings to check
     * @return {@code true} if at least one in the strings is empty or null
     */
    public static boolean isAnyEmpty(final String... ss) {
        return !isNoneEmpty(ss);
    }

    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isAnyEmpty(text, searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String snakeToSplitName(String snakeName, String split) {
        String lowerCase = snakeName.toLowerCase();
        if (isSnakeCase(snakeName)) {
            return replace(lowerCase, "_", split);
        }
        return snakeName;
    }

    private static boolean isWord(char ch) {
        if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
            return true;
        }
        return false;
    }

    private static boolean isWord(String str) {
        if (str == null) {
            return false;
        }
        return str.chars().allMatch(ch -> isWord((char) ch));
    }


    public static boolean isSplitCase(String str, char split) {
        if (str == null) {
            return false;
        }
        return str.chars().allMatch(ch -> ch == split || isWord((char) ch));
    }

    public static String camelToSplitName(String camelName, String split) {
        if (isEmpty(camelName)) {
            return camelName;
        }
        if (!isWord(camelName)) {
            // convert Ab-Cd-Ef to ab-cd-ef
            if (isSplitCase(camelName, split.charAt(0))) {
                return camelName.toLowerCase();
            }
            // not camel case
            return camelName;
        }

        StringBuilder buf = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (buf == null) {
                    buf = new StringBuilder();
                    if (i > 0) {
                        buf.append(camelName, 0, i);
                    }
                }
                if (i > 0) {
                    buf.append(split);
                }
                buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        return buf == null ? camelName.toLowerCase() : buf.toString().toLowerCase();
    }

    /**
     * Convert camelCase or snake_case/SNAKE_CASE to kebab-case
     *
     * @param str
     * @param split
     * @return
     */
    public static String convertToSplitName(String str, String split) {
        if (isSnakeCase(str)) {
            return snakeToSplitName(str, split);
        } else {
            return camelToSplitName(str, split);
        }
    }


}

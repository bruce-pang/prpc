package com.brucepang.prpc.common.url.parse;

import com.brucepang.prpc.common.url.bean.cache.URLItemCache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.brucepang.prpc.common.constants.CommonConstants.DEFAULT_KEY_PREFIX;
import static com.brucepang.prpc.util.StrUtil.EMPTY_STRING;
import static com.brucepang.prpc.util.StrUtil.decodeHexByte;
import static java.lang.Character.*;

/**
 * @author BrucePang
 */
public final class URLStrParser {

    private static final ThreadLocal<TempBuf> DECODE_TEMP_BUF = ThreadLocal.withInitial(() -> new TempBuf(1024));
    private static final char SPACE = 0x20;

    private static Map<String, String> parseEncodedParams(String str, int from) {
        int len = str.length();
        if (from >= len) {
            return Collections.emptyMap();
        }

        TempBuf tempBuf = DECODE_TEMP_BUF.get();
        Map<String, String> params = new HashMap<>();
        int nameStart = from;
        int valueStart = -1;
        int i;
        for (i = from; i < len; i++) {
            char ch = str.charAt(i);
            if (ch == '%') {
                if (i + 3 > len) {
                    throw new IllegalArgumentException("unterminated escape sequence at index " + i + " of: " + str);
                }
                ch = (char) decodeHexByte(str, i + 1);
                i += 2;
            }

            switch (ch) {
                case '=':
                    if (nameStart == i) {
                        nameStart = i + 1;
                    } else if (valueStart < nameStart) {
                        valueStart = i + 1;
                    }
                    break;
                case ';':
                case '&':
                    addParam(str, true, nameStart, valueStart, i - 2, params, tempBuf);
                    nameStart = i + 1;
                    break;
                default:
                    // continue
            }
        }
        addParam(str, true, nameStart, valueStart, i, params, tempBuf);
        return params;
    }

    private static boolean addParam(
            String str,
            boolean isEncoded,
            int nameStart,
            int valueStart,
            int valueEnd,
            Map<String, String> params,
            TempBuf tempBuf) {
        if (nameStart >= valueEnd) {
            return false;
        }

        if (valueStart <= nameStart) {
            valueStart = valueEnd + 1;
        }

        if (isEncoded) {
            String name = decodeComponent(str, nameStart, valueStart - 3, false, tempBuf);
            String value;
            if (valueStart >= valueEnd) {
                value = "";
            } else {
                value = decodeComponent(str, valueStart, valueEnd, false, tempBuf);
            }
            URLItemCache.putParams(params, name, value);
            // compatible with lower versions registering "default." keys
            if (name.startsWith(DEFAULT_KEY_PREFIX)) {
                params.putIfAbsent(name.substring(DEFAULT_KEY_PREFIX.length()), value);
            }
        } else {
            String name = str.substring(nameStart, valueStart - 1);
            String value;
            if (valueStart >= valueEnd) {
                value = "";
            } else {
                value = str.substring(valueStart, valueEnd);
            }
            URLItemCache.putParams(params, name, value);
            // compatible with lower versions registering "default." keys
            if (name.startsWith(DEFAULT_KEY_PREFIX)) {
                params.putIfAbsent(name.substring(DEFAULT_KEY_PREFIX.length()), value);
            }
        }
        return true;
    }

    private static String decodeComponent(String s, int from, int toExcluded, boolean isPath, TempBuf tempBuf) {
        int len = toExcluded - from;
        if (len <= 0) {
            return EMPTY_STRING;
        }

        int firstEscaped = -1;
        for (int i = from; i < toExcluded; i++) {
            char c = s.charAt(i);
            if (c == '%' || c == '+' && !isPath) {
                firstEscaped = i;
                break;
            }
        }
        if (firstEscaped == -1) {
            return s.substring(from, toExcluded);
        }

        // Each encoded byte takes 3 characters (e.g. "%20")
        int decodedCapacity = (toExcluded - firstEscaped) / 3;
        byte[] buf = tempBuf.byteBuf(decodedCapacity);
        char[] charBuf = tempBuf.charBuf(len);
        s.getChars(from, firstEscaped, charBuf, 0);

        int charBufIdx = firstEscaped - from;
        return decodeUtf8Component(s, firstEscaped, toExcluded, isPath, buf, charBuf, charBufIdx);
    }

    private static String decodeUtf8Component(
            String str, int firstEscaped, int toExcluded, boolean isPath, byte[] buf, char[] charBuf, int charBufIdx) {
        int bufIdx;
        for (int i = firstEscaped; i < toExcluded; i++) {
            char c = str.charAt(i);
            if (c != '%') {
                charBuf[charBufIdx++] = c != '+' || isPath ? c : SPACE;
                continue;
            }

            bufIdx = 0;
            do {
                if (i + 3 > toExcluded) {
                    throw new IllegalArgumentException("unterminated escape sequence at index " + i + " of: " + str);
                }
                buf[bufIdx++] = decodeHexByte(str, i + 1);
                i += 3;
            } while (i < toExcluded && str.charAt(i) == '%');
            i--;

            charBufIdx += decodeUtf8(buf, 0, bufIdx, charBuf, charBufIdx);
        }
        return new String(charBuf, 0, charBufIdx);
    }


    public static Map<String, String> parseParams(String rawParam, boolean encoded) {
        if (encoded){
            return URLStrParser.parseEncodedParams(rawParam, 0);
        }
        return parseDecodedParams(rawParam,0);
    }

    private static Map<String, String> parseDecodedParams(String str, int from) {
        int len = str.length();
        if (from >= len) {
            return Collections.emptyMap();
        }

        TempBuf tempBuf = DECODE_TEMP_BUF.get();
        Map<String, String> params = new HashMap<>();
        int nameStart = from;
        int valueStart = -1;
        int i;
        for (i = from; i < len; i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '=':
                    if (nameStart == i) {
                        nameStart = i + 1;
                    } else if (valueStart < nameStart) {
                        valueStart = i + 1;
                    }
                    break;
                case ';':
                case '&':
                    addParam(str, false, nameStart, valueStart, i, params, tempBuf);
                    nameStart = i + 1;
                    break;
                default:
                    // continue
            }
        }
        addParam(str, false, nameStart, valueStart, i, params, tempBuf);
        return params;
    }

    public static int decodeUtf8(byte[] srcBytes, int srcIdx, int srcSize, char[] destChars, int destIdx) {
        // Bitwise OR combines the sign bits so any negative value fails the check.
        if ((srcIdx | srcSize | srcBytes.length - srcIdx - srcSize) < 0
                || (destIdx | destChars.length - destIdx - srcSize) < 0) {
            String exMsg = String.format("buffer srcBytes.length=%d, srcIdx=%d, srcSize=%d, destChars.length=%d, " +
                    "destIdx=%d", srcBytes.length, srcIdx, srcSize, destChars.length, destIdx);
            throw new ArrayIndexOutOfBoundsException(
                    exMsg);
        }

        int offset = srcIdx;
        final int limit = offset + srcSize;
        final int destIdx0 = destIdx;

        // Optimize for 100% ASCII (Hotspot loves small simple top-level loops like this).
        // This simple loop stops when we encounter a byte >= 0x80 (i.e. non-ASCII).
        while (offset < limit) {
            byte b = srcBytes[offset];
            if (!DecodeUtil.isOneByte(b)) {
                break;
            }
            offset++;
            DecodeUtil.handleOneByteSafe(b, destChars, destIdx++);
        }

        while (offset < limit) {
            byte byte1 = srcBytes[offset++];
            if (DecodeUtil.isOneByte(byte1)) {
                DecodeUtil.handleOneByteSafe(byte1, destChars, destIdx++);
                // It's common for there to be multiple ASCII characters in a run mixed in, so add an
                // extra optimized loop to take care of these runs.
                while (offset < limit) {
                    byte b = srcBytes[offset];
                    if (!DecodeUtil.isOneByte(b)) {
                        break;
                    }
                    offset++;
                    DecodeUtil.handleOneByteSafe(b, destChars, destIdx++);
                }
            } else if (DecodeUtil.isTwoBytes(byte1)) {
                if (offset >= limit) {
                    throw new IllegalArgumentException("invalid UTF-8.");
                }
                DecodeUtil.handleTwoBytesSafe(byte1, /* byte2 */ srcBytes[offset++], destChars, destIdx++);
            } else if (DecodeUtil.isThreeBytes(byte1)) {
                if (offset >= limit - 1) {
                    throw new IllegalArgumentException("invalid UTF-8.");
                }
                DecodeUtil.handleThreeBytesSafe(
                        byte1,
                        /* byte2 */ srcBytes[offset++],
                        /* byte3 */ srcBytes[offset++],
                        destChars,
                        destIdx++);
            } else {
                if (offset >= limit - 2) {
                    throw new IllegalArgumentException("invalid UTF-8.");
                }
                DecodeUtil.handleFourBytesSafe(
                        byte1,
                        /* byte2 */ srcBytes[offset++],
                        /* byte3 */ srcBytes[offset++],
                        /* byte4 */ srcBytes[offset++],
                        destChars,
                        destIdx);
                destIdx += 2;
            }
        }
        return destIdx - destIdx0;
    }

    private static class DecodeUtil {

        /**
         * Returns whether this is a single-byte codepoint (i.e., ASCII) with the form '0XXXXXXX'.
         */
        private static boolean isOneByte(byte b) {
            return b >= 0;
        }

        /**
         * Returns whether this is a two-byte codepoint with the form '10XXXXXX'.
         */
        private static boolean isTwoBytes(byte b) {
            return b < (byte) 0xE0;
        }

        /**
         * Returns whether this is a three-byte codepoint with the form '110XXXXX'.
         */
        private static boolean isThreeBytes(byte b) {
            return b < (byte) 0xF0;
        }

        private static void handleOneByteSafe(byte byte1, char[] resultArr, int resultPos) {
            resultArr[resultPos] = (char) byte1;
        }

        private static void handleTwoBytesSafe(byte byte1, byte byte2, char[] resultArr, int resultPos) {
            checkUtf8(byte1, byte2);
            resultArr[resultPos] = (char) (((byte1 & 0x1F) << 6) | trailingByteValue(byte2));
        }

        private static void checkUtf8(byte byte1, byte byte2) {
            // Simultaneously checks for illegal trailing-byte in leading position (<= '11000000') and
            // overlong 2-byte, '11000001'.
            if (byte1 < (byte) 0xC2 || isNotTrailingByte(byte2)) {
                throw new IllegalArgumentException("invalid UTF-8.");
            }
        }

        private static void handleThreeBytesSafe(byte byte1, byte byte2, byte byte3, char[] resultArr, int resultPos) {
            checkUtf8(byte1, byte2, byte3);
            resultArr[resultPos] =
                    (char) (((byte1 & 0x0F) << 12) | (trailingByteValue(byte2) << 6) | trailingByteValue(byte3));
        }

        private static void checkUtf8(byte byte1, byte byte2, byte byte3) {
            if (isNotTrailingByte(byte2)
                    // overlong? 5 most significant bits must not all be zero
                    || (byte1 == (byte) 0xE0 && byte2 < (byte) 0xA0)
                    // check for illegal surrogate codepoints
                    || (byte1 == (byte) 0xED && byte2 >= (byte) 0xA0)
                    || isNotTrailingByte(byte3)) {
                throw new IllegalArgumentException("invalid UTF-8.");
            }
        }

        private static void handleFourBytesSafe(byte byte1, byte byte2, byte byte3, byte byte4, char[] resultArr,
                                                int resultPos) {
            checkUtf8(byte1, byte2, byte3, byte4);
            int codepoint =
                    ((byte1 & 0x07) << 18)
                            | (trailingByteValue(byte2) << 12)
                            | (trailingByteValue(byte3) << 6)
                            | trailingByteValue(byte4);

            resultArr[resultPos] = DecodeUtil.highSurrogate(codepoint);
            resultArr[resultPos + 1] = DecodeUtil.lowSurrogate(codepoint);
        }

        private static void checkUtf8(byte byte1, byte byte2, byte byte3, byte byte4) {
            if (isNotTrailingByte(byte2)
                    // Check that 1 <= plane <= 16.  Tricky optimized form of:
                    //   valid 4-byte leading byte?
                    // if (byte1 > (byte) 0xF4 ||
                    //   overlong? 4 most significant bits must not all be zero
                    //     byte1 == (byte) 0xF0 && byte2 < (byte) 0x90 ||
                    //   codepoint larger than the highest code point (U+10FFFF)?
                    //     byte1 == (byte) 0xF4 && byte2 > (byte) 0x8F)
                    || (((byte1 << 28) + (byte2 - (byte) 0x90)) >> 30) != 0
                    || isNotTrailingByte(byte3)
                    || isNotTrailingByte(byte4)) {
                throw new IllegalArgumentException("invalid UTF-8.");
            }
        }

        /**
         * Returns whether the byte is not a valid continuation of the form '10XXXXXX'.
         */
        private static boolean isNotTrailingByte(byte b) {
            return b > (byte) 0xBF;
        }

        /**
         * Returns the actual value of the trailing byte (removes the prefix '10') for composition.
         */
        private static int trailingByteValue(byte b) {
            return b & 0x3F;
        }

        private static char highSurrogate(int codePoint) {
            return (char)
                    ((MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT >>> 10)) + (codePoint >>> 10));
        }

        private static char lowSurrogate(int codePoint) {
            return (char) (MIN_LOW_SURROGATE + (codePoint & 0x3ff));
        }
    }

}

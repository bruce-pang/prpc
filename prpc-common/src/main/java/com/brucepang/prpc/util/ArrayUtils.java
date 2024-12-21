package com.brucepang.prpc.util;

/**
 * Contains some methods to check array.
 */
public final class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * <p>Checks if the array is null or empty. <p/>
     *
     * @param array th array to check
     * @return {@code true} if the array is null or empty.
     */
    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * <p>Checks if the array is not null or empty. <p/>
     *
     * @param array th array to check
     * @return {@code true} if the array is not null or empty.
     */
    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

    public static boolean contains(final String[] array, String valueToFind) {
        return indexOf(array, valueToFind, 0) != -1;
    }

    public static int indexOf(String[] array, String valueToFind, int startIndex) {
        if (isEmpty(array) || valueToFind == null) {
            return -1;
        } else {
            if (startIndex < 0) {
                startIndex = 0;
            }

            for (int i = startIndex; i < array.length; ++i) {
                if (valueToFind.equals(array[i])) {
                    return i;
                }
            }

            return -1;
        }
    }

    /**
     * Convert from variable arguments to array
     *
     * @param values variable arguments
     * @param <T>    The class
     * @return array
     * @since 2.7.9
     */
    public static <T> T[] of(T... values) {
        return values;
    }
}

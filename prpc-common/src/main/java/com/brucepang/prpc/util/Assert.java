package com.brucepang.prpc.util;

public class Assert {

    public static void assertEquals(Object expected, Object actual) {
        if ((expected == null && actual != null) || !expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but got false");
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but got true");
        }
    }

    public static void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Expected object to be not null");
        }
    }

    public static void assertNull(Object object) {
        if (object != null) {
            throw new AssertionError("Expected object to be null");
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmptyString(String str, String message) {
        if (StrUtil.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

}
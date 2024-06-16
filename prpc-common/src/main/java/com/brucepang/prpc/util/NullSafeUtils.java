package com.brucepang.prpc.util;

import java.util.Optional;
import java.util.function.Supplier;
/**
 * Null safe utils
 *
 * @author BrucePang
 */
public class NullSafeUtils {

    public static <T> Optional<T> nullSafe(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
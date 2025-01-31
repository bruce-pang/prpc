package com.brucepang.prpc.util;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * ConcurrentHashMap util
 */
public class ConcurrentHashMapUtils {


    public static <K, V> V computeIfAbsent(ConcurrentMap<K, V> map, K key, Function<? super K, ? extends V> func) {
        Objects.requireNonNull(func);
        if (JRE.JAVA_8.isCurrentVersion()) {
            V v = map.get(key);
            if (null == v) {
                // issue#11986 lock bug
                // v = map.computeIfAbsent(key, func);

                // this bug fix methods maybe cause `func.apply` multiple calls.
                v = func.apply(key);
                if (null == v) {
                    return null;
                }
                final V res = map.putIfAbsent(key, v);
                if (null != res) {
                    // if pre value present, means other thread put value already, and putIfAbsent not effect
                    // return exist value
                    return res;
                }
                // if pre value is null, means putIfAbsent effected, return current value
            }
            return v;
        } else {
            return map.computeIfAbsent(key, func);
        }
    }
}

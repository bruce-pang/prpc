package com.brucepang.prpc.common.constants;

import java.util.regex.Pattern;

/**
 * @author BrucePang
 */
public interface CommonConstants {
    String METHODS_KEY = "methods";

    String METHOD_KEY = "method";

    String TIMESTAMP_KEY = "timestamp";

    String DEFAULT_KEY_PREFIX = "default.";

    Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
}

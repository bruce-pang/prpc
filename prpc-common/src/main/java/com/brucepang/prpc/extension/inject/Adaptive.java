package com.brucepang.prpc.extension.inject;

import com.brucepang.prpc.extension.ExtensionLoader;
import com.brucepang.prpc.common.URL;

/**
 * Provide helpful information for {@link ExtensionLoader} to inject dependency extension instance.
 *
 * @see ExtensionLoader
 * @see URL
 * @author BrucePang
 */
public @interface Adaptive {
    /**
     * The value of adaptive extension key.
     * @return the value of adaptive extension key.
     */
    String[] value() default {};
}

package com.brucepang.prpc.extension;

import java.lang.annotation.*;

/**
 * Marker for extension interface
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {
    /**
     *
     * default loader extension name
     */
    String value() default "";

    /**
     * SPI Extension Scope
     */
    ExtensionScope scope() default ExtensionScope.GLOBAL;
}

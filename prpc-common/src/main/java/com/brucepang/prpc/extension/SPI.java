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
     * 默认加载的扩展名
     */
    String value() default "";

    /**
     * 扩展点的范围
     */
    ExtensionScope scope() default ExtensionScope.GLOBAL;
}

package com.brucepang.prpc.config.annotation;

import java.lang.annotation.*;

/**
 * An annotation used for referencing a`PRPC` service.
 * @author BrucePang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface PRPCReference {

    /**
     * Interface class name, default value is empty string
     */
    String interfaceName() default "";

    boolean init() default true;

}

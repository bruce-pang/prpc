package com.brucepang.prpc.extension.inject;

import java.lang.annotation.*;

/**
 * Disable Inject
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DisableInject {
}

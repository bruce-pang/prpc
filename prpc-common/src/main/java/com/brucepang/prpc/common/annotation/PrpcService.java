package com.brucepang.prpc.common.annotation;

import com.brucepang.prpc.common.loadblance.LoadBalanceRules;

import java.lang.annotation.*;

/**
 * Class-level annotation used for declaring PRPC service.
 *
 * @use @PrpcService()
 *
 * @author BrucePang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface PrpcService {
    /**
     * Interface class, default value is void.class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * Interface class name, default value is empty string
     */
    String interfaceName() default "";


    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     *
     * you can use {@link LoadBalanceRules#ROUND_ROBIN} ……
     */
    String loadBalance() default LoadBalanceRules.ROUND_ROBIN;
}

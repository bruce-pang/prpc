package com.brucepang.prpc.common.compact;

/**
 * @author BrucePang
 */
public class PrpcGenericExceptionUtils {
    private static final String GENERIC_EXCEPTION_CLASS_NAME = "com.brucepang.prpc.common.rpc.exception.GenericException";

    public static boolean isGenericException(Throwable e) {
        return e != null && GENERIC_EXCEPTION_CLASS_NAME.equals(e.getClass().getName());
    }
}

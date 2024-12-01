package com.brucepang.prpc.common.rpc;

/**
 * @author BrucePang
 */
public class GenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }
}

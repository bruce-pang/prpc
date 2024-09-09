package com.brucepang.prpc.remoting;

/**
 * @author BrucePang
 */
public class TimeoutException extends RemotingException {

    public TimeoutException(String message) {
        super(null, null, message);
    }


}

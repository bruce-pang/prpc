package com.brucepang.prpc.exception;

/**
 * Prpc全局异常类
 * @author BrucePang
 */
public class PrpcException extends RuntimeException{
    private int errorCode;
    private String errorMessage;

    public PrpcException(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

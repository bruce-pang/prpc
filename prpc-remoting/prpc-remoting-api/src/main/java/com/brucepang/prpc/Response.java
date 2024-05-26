package com.brucepang.prpc;

public class Response {

    public enum Status {
        OK,
        ERROR,
        // Add more status as needed
    }

    private Status status;
    private Object result;
    private Throwable error;

    // getters and setters
    // ...

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
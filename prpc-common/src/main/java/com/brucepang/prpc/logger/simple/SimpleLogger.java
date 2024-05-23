package com.brucepang.prpc.logger.simple;

import com.brucepang.prpc.logger.Logger;

public class SimpleLogger implements Logger {
    private String name;

    public SimpleLogger(String name) {
        this.name = name;
    }


    @Override
    public void debug(String message) {
        System.out.println("DEBUG: " + name + ": " + message);
    }

    @Override
    public void debug(Throwable e) {
        System.out.println("DEBUG: " + name + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }

    @Override
    public void debug(String message, Throwable e) {
        System.out.println("DEBUG: " + name + ": " + message);
        e.printStackTrace(System.out);
    }

    @Override
    public void info(String message) {
        System.out.println("INFO: " + name + ": " + message);
    }

    @Override
    public void info(Throwable e) {
        System.out.println("INFO: " + name + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }

    @Override
    public void info(String message, Throwable e) {
        System.out.println("INFO: " + name + ": " + message);
        e.printStackTrace(System.out);
    }

    @Override
    public void warn(String message) {
        System.out.println("WARN: " + name + ": " + message);
    }

    @Override
    public void warn(Throwable e) {
        System.out.println("WARN: " + name + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }

    @Override
    public void warn(String message, Throwable e) {
        System.out.println("WARN: " + name + ": " + message);
        e.printStackTrace(System.out);
    }

    @Override
    public void error(String message) {
        System.out.println("ERROR: " + name + ": " + message);
    }

    @Override
    public void error(Throwable e) {
        System.out.println("ERROR: " + name + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }

    @Override
    public void error(String message, Throwable e) {
        System.out.println("ERROR: " + name + ": " + message);
        e.printStackTrace(System.out);
    }
}
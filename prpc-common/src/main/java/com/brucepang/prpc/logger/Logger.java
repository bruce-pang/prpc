package com.brucepang.prpc.logger;

/**
 * Logger interface
 */
public interface Logger {
    /**
     * Logs a message with debug log level.
     *
     * @param message log this message
     */
    void debug(String message);

    /**
     * Logs an error with debug log level.
     * @param e log this cause
     */
    void debug(Throwable e);

    /**
     * Logs an error with debug log level.
     * @param message log this message
     * @param e log this cause
     */
    void debug(String message, Throwable e);



    /**
     * Logs a message with info log level.
     *
     * @param message log this message
     */
    void info(String message);

    /**
     * Logs an error with info log level.
     * @param e log this cause
     */
    void info(Throwable e);

    /**
     * Logs an error with info log level.
     * @param message log this message
     * @param e log this cause
     */
    void info(String message, Throwable e);

    /**
     * Logs a message with warn log level.
     *
     * @param message log this message
     */
    void warn(String message);

    /**
     * Logs an error with warn log level.
     * @param e log this cause
     */
    void warn(Throwable e);

    /**
     * Logs an error with warn log level.
     * @param message log this message
     * @param e log this cause
     */
    void warn(String message, Throwable e);

    /**
     * Logs a message with error log level.
     *
     * @param message log this message
     */
    void error(String message);

    /**
     * Logs an error with error log level.
     * @param e log this cause
     */
    void error(Throwable e);

    /**
     * Logs an error with error log level.
     * @param message log this message
     * @param e log this cause
     */
    void error(String message, Throwable e);


}
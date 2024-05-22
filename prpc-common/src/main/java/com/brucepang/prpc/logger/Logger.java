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
     * Logs a message with info log level.
     *
     * @param message log this message
     */
    void info(String message);

    /**
     * Logs a message with warn log level.
     *
     * @param message log this message
     */
    void warn(String message);

    /**
     * Logs a message with error log level.
     *
     * @param message log this message
     */
    void error(String message);


}
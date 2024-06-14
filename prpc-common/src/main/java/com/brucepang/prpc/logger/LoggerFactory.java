package com.brucepang.prpc.logger;

import com.brucepang.prpc.logger.slf4j.Slf4jLoggerAdapter;
import com.brucepang.prpc.logger.support.GenerateStandAloneLogger;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoggerFactory {
    private static final ConcurrentMap<String, GenerateStandAloneLogger> LOGGERS = new ConcurrentHashMap<>();
    private static volatile LoggerAdapter LOGGER_ADAPTER;

    private LoggerFactory() {
    }

    static {
        String logger = System.getProperty("prpc.application.logger", "");
        switch (logger){
            case "slf4j":
                setLoggerAdapter(new Slf4jLoggerAdapter());
                break;
            default:
                List<Class<? extends LoggerAdapter>> candidates = Arrays.asList(Slf4jLoggerAdapter.class);
                for (Class<? extends LoggerAdapter> clazz : candidates) {
                    try {
                        LoggerAdapter loggerAdapter = clazz.newInstance();
                        loggerAdapter.getLogger(LoggerFactory.class);
                        setLoggerAdapter(loggerAdapter);
                        break;
                    } catch (Throwable ignored) {
                    }
                }
        }
    }

    /**
     * Get logger provider by class
     * @param clazz class
     * @return logger
     */
    public static <T> Logger getLogger(Class<T> clazz) {
        return LOGGERS.computeIfAbsent(clazz.getName(), name -> new GenerateStandAloneLogger(LOGGER_ADAPTER.getLogger(name)));
    }

    /**
     * Get logger provider by key
     * @param loggerAdapter logger provider
     */
    public static void setLoggerAdapter(LoggerAdapter loggerAdapter) {
        if (loggerAdapter != null) {
            if (loggerAdapter == LOGGER_ADAPTER) {
                return;
            }
            loggerAdapter.getLogger(LoggerFactory.class.getName());
            LoggerFactory.LOGGER_ADAPTER = loggerAdapter;
            for (Map.Entry<String, GenerateStandAloneLogger> entry : LOGGERS.entrySet()) {
                entry.getValue().setLogger(LOGGER_ADAPTER.getLogger(entry.getKey()));
            }
        }
    }

    /**
     * Get logger provider
     *
     * @param key the returned logger will be named after key
     * @return logger provider
     */
    public static Logger getLogger(String key) {
        return LOGGERS.computeIfAbsent(key, k -> new GenerateStandAloneLogger(LOGGER_ADAPTER.getLogger(k)));
    }

    /**
     * Get logging level
     *
     * @return logging level
     */
    public static Level getLevel() {
        return LOGGER_ADAPTER.getLevel();
    }

    /**
     * Set the current logging level
     *
     * @param level logging level
     */
    public static void setLevel(Level level) {
        LOGGER_ADAPTER.setLevel(level);
    }

    /**
     * Get the current logging file
     *
     * @return current logging file
     */
    public static File getFile() {
        return LOGGER_ADAPTER.getFile();
    }
}
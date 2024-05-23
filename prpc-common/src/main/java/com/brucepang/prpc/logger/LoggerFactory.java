package com.brucepang.prpc.logger;

import com.brucepang.prpc.logger.simple.SimpleLogger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoggerFactory {
    private static final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();

    public static Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, SimpleLogger::new);
    }

    public static <T> Logger getLogger(Class<T> clazz) {
        return loggers.computeIfAbsent(clazz.getName(), SimpleLogger::new);
    }
}
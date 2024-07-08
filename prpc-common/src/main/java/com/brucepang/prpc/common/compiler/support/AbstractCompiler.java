package com.brucepang.prpc.common.compiler.support;

import com.brucepang.prpc.common.compiler.Compiler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AbstractCompiler. (SPI, Prototype, ThreadSafe)
 * @author BrucePang
 */
public abstract class AbstractCompiler implements Compiler {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");

    private static final Map<String, Lock> CLASS_IN_CREATION_MAP = new ConcurrentHashMap<>();

    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        String className = extractClassName(code);
        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("Unable to extract class name from code");
        }

        // Check whether a lock already exists for the class name
        Lock lock = CLASS_IN_CREATION_MAP.get(className);
        if (lock == null) {
            // Create a new lock if it does not exist
            lock = new ReentrantLock();
            CLASS_IN_CREATION_MAP.put(className, lock);
        }

        // Acquire the lock before compiling the class
        lock.lock();
        try {
            // Compile the class
            return doCompile(className, code, classLoader);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // Release the lock after compiling the class
            lock.unlock();
        }
    }

    private String extractClassName(String code) {
        Matcher matcher = CLASS_PATTERN.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 1. use the code to compile a class
     * 2. load the class
     * @param name class name
     * @param code java source code
     * @param classLoader class loader
     * @return compiled class
     * @throws Throwable
     */
    protected  Class<?> doCompile(String name ,String code, ClassLoader classLoader) throws Throwable {
        return null;
    }

}

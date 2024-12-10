package com.brucepang.prpc.metadata.definition.util;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author BrucePang
 */
public final class ClassUtils {
    /**
     * Get the code source file or class path of the Class passed in.
     *
     * @param clazz
     * @return Jar file name or class path.
     */
    public static String getCodeSource(Class<?> clazz) {
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        if (protectionDomain == null || protectionDomain.getCodeSource() == null) {
            return null;
        }

        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        URL location = codeSource.getLocation();
        if (location == null) {
            return null;
        }

        String path = location.toExternalForm();

        if (path.endsWith(".jar") && path.contains("/")) {
            return path.substring(path.lastIndexOf('/') + 1);
        }
        return path;
    }
}

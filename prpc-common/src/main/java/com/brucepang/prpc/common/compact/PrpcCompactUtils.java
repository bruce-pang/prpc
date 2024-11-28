package com.brucepang.prpc.common.compact;

import com.brucepang.prpc.common.constants.CommonConstants;
import com.brucepang.prpc.util.StrUtil;

import java.lang.annotation.Annotation;

/**
 * @author BrucePang
 */
public class PrpcCompactUtils {
    private static volatile boolean enabled = true;
    private static final Class<? extends Annotation> REFERENCE_CLASS;
    private static final Class<? extends Annotation> SERVICE_CLASS;
    private static final Class<?> ECHO_SERVICE_CLASS;
    private static final Class<?> GENERIC_SERVICE_CLASS;

    static {
        initEnabled();
        REFERENCE_CLASS = loadAnnotation("com.brucepang.prpc.config.annotation.Reference");
        SERVICE_CLASS = loadAnnotation("com.brucepang.prpc.config.annotation.Service");
        ECHO_SERVICE_CLASS = loadClass("com.brucepang.prpc.rpc.service.EchoService");
        GENERIC_SERVICE_CLASS = loadClass("com.brucepang.prpc.rpc.service.GenericService");
    }

    private static void initEnabled() {
        try {
            String fromProp = System.getProperty(CommonConstants.PRPC_COMPACT_ENABLE);
            if (StrUtil.isNotEmpty(fromProp)) {
                enabled = Boolean.parseBoolean(fromProp);
                return;
            }
            String fromEnv = System.getenv(CommonConstants.PRPC_COMPACT_ENABLE);
            if (StrUtil.isNotEmpty(fromEnv)) {
                enabled = Boolean.parseBoolean(fromEnv);
                return;
            }
            fromEnv = System.getenv(StrUtil.toOSStyleKey(CommonConstants.PRPC_COMPACT_ENABLE));
            enabled = !StrUtil.isNotEmpty(fromEnv) || Boolean.parseBoolean(fromEnv);
        } catch (Throwable t) {
            enabled = true;
        }
    }

    private static Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (Throwable e) {
            return null;
        }
    }

    private static Class<? extends Annotation> loadAnnotation(String name) {
        try {
            Class<?> clazz = Class.forName(name);
            if (clazz.isAnnotation()) {
                return (Class<? extends Annotation>) clazz;
            } else {
                return null;
            }
        } catch (Throwable e) {
            return null;
        }
    }
}

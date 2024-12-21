package com.brucepang.prpc.metadata.definition.util;

import com.brucepang.prpc.util.StrUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author BrucePang
 */
public class JaketConfigurationUtils {

    private static final String CONFIGURATION_FILE = "jaket.properties";

    private static String[] includedTypePackages;

    private static String[] includedInterfacePackages;

    private static String[] closedTypes;

    static {
        Properties props = new Properties();
        InputStream inStream = JaketConfigurationUtils.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE);
        try {
            props.load(inStream);
            String value = props.getProperty("included_type_packages");
            if (StrUtil.isNotEmpty(value)) {
                includedTypePackages = value.split(",");
            }
        } catch (Throwable ignored) {

        }
    }


    public static boolean isExcludedType(Class<?> clazz) {
        if (includedTypePackages == null || includedTypePackages.length == 0) {
            return false;
        }

        for (String packagePrefix : includedTypePackages) {
            if (clazz.getCanonicalName().startsWith(packagePrefix)) {
                return false;
            }
        }

        return true;
    }
}

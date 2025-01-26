package com.brucepang.prpc.config;

import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.ScopeModel;
import com.brucepang.prpc.util.CollectionUtils;
import com.brucepang.prpc.util.StrUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ConfigurationUtils
 * @author BrucePang
 */
public final class ConfigurationUtils {

    /**
     * Forbids instantiation.
     */
    private ConfigurationUtils() {
        throw new UnsupportedOperationException("No instance of 'ConfigurationUtils' for you! ");
    }

    private static ScopeModel getScopeModelOrDefaultApplicationModel(ScopeModel realScopeModel) {
        if (realScopeModel == null) {
            return ApplicationModel.defaultModel();
        }
        return realScopeModel;
    }

    /**
     * Used to get properties from the os environment
     *
     * @return
     */
    public static Configuration getEnvConfiguration(ScopeModel scopeModel) {
        return getScopeModelOrDefaultApplicationModel(scopeModel)
                .modelEnvironment()
                .getEnvironmentConfiguration();
    }

    public static String getProperty(ApplicationModel applicationModel, String prpcLabels) {
        return (String) getEnvConfiguration(applicationModel).getInternalProperty(prpcLabels);
    }

    public static boolean isEmptyValue(Object value) {
        return value == null || value instanceof String && StrUtil.isBlank((String) value);
    }

    private static <V extends Object> Map<String, V> getSubProperties(
            Map<String, V> configMap, String prefix, Map<String, V> resultMap) {
        if (!prefix.endsWith(".")) {
            prefix += ".";
        }

        if (null == resultMap) {
            resultMap = new LinkedHashMap<>();
        }

        if (CollectionUtils.isNotEmptyMap(configMap)) {
            Map<String, V> copy;
            synchronized (configMap) {
                copy = new HashMap<>(configMap);
            }
            for (Map.Entry<String, V> entry : copy.entrySet()) {
                String key = entry.getKey();
                V val = entry.getValue();
                if (StrUtil.startsWithIgnoreCase(key, prefix)
                        && key.length() > prefix.length()
                        && !ConfigurationUtils.isEmptyValue(val)) {

                    String k = key.substring(prefix.length());
                    // convert camelCase/snake_case to kebab-case
                    String newK = StrUtil.convertToSplitName(k, "-");
                    resultMap.putIfAbsent(newK, val);
                    if (!Objects.equals(k, newK)) {
                        resultMap.putIfAbsent(k, val);
                    }
                }
            }
        }

        return resultMap;
    }

}

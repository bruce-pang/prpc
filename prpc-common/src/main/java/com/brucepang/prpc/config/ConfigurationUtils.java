package com.brucepang.prpc.config;

import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.ScopeModel;

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
}

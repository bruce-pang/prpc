package com.brucepang.prpc.scope.processor;

import com.brucepang.prpc.extension.ExtensionPostProcessor;

/**
 * todo: implement this class to process the extension before or after initialization
 * @author BrucePang
 */
public class ScopeModelAwareExtensionProcessor implements ExtensionPostProcessor{
    @Override
    public Object postProcessBeforeInitialization(Object instance, String name) throws Exception {
        return ExtensionPostProcessor.super.postProcessBeforeInitialization(instance, name);
    }

    @Override
    public Object postProcessAfterInitialization(Object instance, String name) throws Exception {
        return ExtensionPostProcessor.super.postProcessAfterInitialization(instance, name);
    }
}

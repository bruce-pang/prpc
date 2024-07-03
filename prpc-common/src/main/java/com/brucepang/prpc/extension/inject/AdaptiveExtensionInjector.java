package com.brucepang.prpc.extension.inject;

import com.brucepang.prpc.extension.ExtensionInjector;

/**
 * AdaptiveExtensionInjector
 * @author BrucePang
 */
public class AdaptiveExtensionInjector implements ExtensionInjector {

    @Override
    public <T> T getInstance(Class<T> type, String name) {
        // todo: inject extension instance by type and name
        return null;
    }
}

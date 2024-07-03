package com.brucepang.prpc.extension.inject;

import com.brucepang.prpc.extension.ExtensionAccessor;
import com.brucepang.prpc.extension.ExtensionInjector;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AdaptiveExtensionInjector
 * @author BrucePang
 */
public class AdaptiveExtensionInjector implements ExtensionInjector {
    private Collection<ExtensionInjector> injectors = Collections.emptyList();
    private ExtensionAccessor extensionAccessor;

    public AdaptiveExtensionInjector() {
    }

    public void initialize() throws IllegalStateException {
        injectors = extensionAccessor.getExtensionLoader(ExtensionInjector.class)
                .getSupportedExtensions().stream()
                .map(name -> extensionAccessor.getExtension(ExtensionInjector.class, name))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public <T> T getInstance(Class<T> type, String name) {
        // Traversal: Instances corresponding to the three locations of spi/spring/scopeBean
        // Get a direct return
        return injectors.stream()
                .map(injector -> injector.getInstance(type, name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}

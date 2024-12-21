package com.brucepang.prpc.common.compiler.support;

import com.brucepang.prpc.common.compiler.Compiler;
import com.brucepang.prpc.extension.ExtensionLoader;
import com.brucepang.prpc.scope.model.GlobalModel;

/**
 * AdaptiveCompiler. (SPI, Prototype, ThreadSafe)
 * @author BrucePang
 */
public class AdaptiveCompiler implements Compiler {
    private GlobalModel globalModel;

    public AdaptiveCompiler(GlobalModel globalModel) {
        this.globalModel = globalModel;
    }

    private static volatile String DEFAULT_COMPILER;

    public static void setDefaultCompiler(String compiler) {
        DEFAULT_COMPILER = compiler;
    }

    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        Compiler compiler;
        ExtensionLoader<Compiler> extensionLoader = globalModel.getExtensionLoader(Compiler.class);
        String name = DEFAULT_COMPILER; // copy reference
        if (name != null && name.length() > 0) {
            compiler = extensionLoader.getExtension(name);
        } else {
            compiler = extensionLoader.getDefaultExtension();
        }
        return compiler.compile(code, classLoader);
    }
}

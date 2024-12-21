package com.brucepang.prpc.common.compiler;

import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;

/**
 * Compiler interface.(SPI, Singleton, ThreadSafe)
 * @author BrucePang
 */
@SPI(value = "javassist", scope = ExtensionScope.GLOBAL)
public interface Compiler {
    /**
     * Compile java source code to class.
     *
     * @param code java source code
     * @param classLoader class loader
     */
    Class<?> compile(String code, ClassLoader classLoader);
}

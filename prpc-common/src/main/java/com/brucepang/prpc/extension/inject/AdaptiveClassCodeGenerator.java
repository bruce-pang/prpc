package com.brucepang.prpc.extension.inject;

import com.brucepang.prpc.extension.ExtensionLoader;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * generator the Adaptive class
 * @author BrucePang
 */
public class AdaptiveClassCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(AdaptiveClassCodeGenerator.class);

    /**
     * Generate the code of the adaptive class
     * @param type the type of the adaptive class
     * @return
     */
    private String createAdaptiveExtensionClassCode(Class<?> type) {
        StringBuilder code = new StringBuilder();
        code.append("package ").append(type.getPackage().getName()).append(";");
        code.append("import ").append(ExtensionLoader.class.getName()).append(";");
        code.append("public class ").append(type.getSimpleName()).append("$Adaptive implements ").append(type.getCanonicalName()).append(" {");
        code.append("public ").append(type.getSimpleName()).append("$Adaptive(){}");
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            code.append("public ").append(method.getReturnType().getCanonicalName()).append(" ").append(method.getName()).append("(");
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) {
                    code.append(",");
                }
                code.append(parameterTypes[i].getCanonicalName()).append(" arg").append(i);
            }
            code.append("){");
            code.append("String methodName = \"").append(method.getName()).append("\";");
            code.append("String[] argTypes = new String[").append(parameterTypes.length).append("];");
            for (int i = 0; i < parameterTypes.length; i++) {
                code.append("argTypes[").append(i).append("] = arg").append(i).append(".getClass().getName();");
            }
            code.append("ExtensionLoader loader = ExtensionLoader.getExtensionLoader(").append(type.getCanonicalName()).append(".class);");
            code.append("ExtensionAccessor accessor = loader.getExtensionAccessor();");
            code.append("return (").append(method.getReturnType().getCanonicalName()).append(")accessor.invoke(methodName, argTypes, new Object[]{").append(Stream.of(parameterTypes).map(p -> "arg" + p).collect(Collectors.joining(","))).append("});");
            code.append("}");
        }
        code.append("}");
        return code.toString();
    }
}

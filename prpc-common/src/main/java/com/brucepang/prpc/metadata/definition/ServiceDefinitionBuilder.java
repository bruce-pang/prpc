package com.brucepang.prpc.metadata.definition;

import com.brucepang.prpc.common.compiler.support.ClassUtils;
import com.brucepang.prpc.metadata.definition.model.MethodDefinition;
import com.brucepang.prpc.metadata.definition.model.ServiceDefinition;
import com.brucepang.prpc.metadata.definition.model.TypeDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author BrucePang
 */
public final class ServiceDefinitionBuilder {

    private ServiceDefinitionBuilder() {}

    public static <T extends ServiceDefinition> void build(T sd, final Class<?> interfaceClass) {
        sd.setCanonicalName(interfaceClass.getCanonicalName());
        sd.setCodeSource(ClassUtils.getCodeSource(interfaceClass));
        Annotation[] classAnnotations = interfaceClass.getAnnotations();
        sd.setAnnotations(annotationToStringList(classAnnotations));

        TypeDefinitionBuilder builder = new TypeDefinitionBuilder();
        List<Method> methods = ClassUtils.getPublicNonStaticMethods(interfaceClass);
        for (Method method : methods) {
            MethodDefinition md = new MethodDefinition();
            md.setName(method.getName());

            Annotation[] methodAnnotations = method.getAnnotations();
            md.setAnnotations(annotationToStringList(methodAnnotations));

            // Process parameter types.
            Class<?>[] paramTypes = method.getParameterTypes();
            Type[] genericParamTypes = method.getGenericParameterTypes();

            String[] parameterTypes = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                TypeDefinition td = builder.build(genericParamTypes[i], paramTypes[i]);
                parameterTypes[i] = td.getType();
            }
            md.setParameterTypes(parameterTypes);

            // Process return type.
            TypeDefinition td = builder.build(method.getGenericReturnType(), method.getReturnType());
            md.setReturnType(td.getType());

            sd.getMethods().add(md);
        }

        sd.setTypes(builder.getTypeDefinitions());
    }

    private static List<String> annotationToStringList(Annotation[] annotations) {
        if (annotations == null) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        for (Annotation annotation : annotations) {
            list.add(annotation.toString());
        }
        return list;
    }

    /**
     * Describe a Java interface in {@link ServiceDefinition}.
     *
     * @return Service description
     */
    public static ServiceDefinition build(final Class<?> interfaceClass) {
        ServiceDefinition sd = new ServiceDefinition();
        build(sd, interfaceClass);
        return sd;
    }


}

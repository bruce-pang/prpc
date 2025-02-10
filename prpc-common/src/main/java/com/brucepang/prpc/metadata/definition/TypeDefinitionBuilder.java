package com.brucepang.prpc.metadata.definition;

import com.brucepang.prpc.common.compiler.support.ClassUtils;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.metadata.definition.builder.DefaultTypeBuilder;
import com.brucepang.prpc.metadata.definition.builder.TypeBuilder;
import com.brucepang.prpc.metadata.definition.model.TypeDefinition;
import com.brucepang.prpc.scope.model.GlobalModel;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author BrucePang
 */
public class TypeDefinitionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TypeDefinitionBuilder.class);
    public static List<TypeBuilder> BUILDERS;
    private final Map<String, TypeDefinition> typeCache = new HashMap<>();

    public static void initBuilders(GlobalModel model) {
        Set<TypeBuilder> tbs = model.getExtensionLoader(TypeBuilder.class).getSupportedExtensionInstances();
        BUILDERS = new ArrayList<>(tbs);
    }

    private static TypeBuilder getGenericTypeBuilder(Class<?> clazz) {
        for (TypeBuilder builder : BUILDERS) {
            try {
                if (builder.accept(clazz)) {
                    return builder;
                }
            } catch (NoClassDefFoundError cnfe) {
                // ignore
                logger.info("Throw classNotFound (" + cnfe.getMessage() + ") in " + builder.getClass());
            }
        }
        return null;
    }

    public TypeDefinition build(Type type, Class<?> clazz) {
        return build(type, clazz, typeCache);
    }

    public static TypeDefinition build(Type type, Class<?> clazz, Map<String, TypeDefinition> typeCache) {
        TypeBuilder builder = getGenericTypeBuilder(clazz);
        TypeDefinition td;

        if (clazz.isPrimitive() || ClassUtils.isSimpleType(clazz)) { // changed since 2.7.6
            td = new TypeDefinition(clazz.getCanonicalName());
            typeCache.put(clazz.getCanonicalName(), td);
        } else if (builder != null) {
            td = builder.build(type, clazz, typeCache);
        } else {
            td = DefaultTypeBuilder.build(clazz, typeCache);
        }
        return td;
    }

    public List<TypeDefinition> getTypeDefinitions() {
        return new ArrayList<>(typeCache.values());
    }
}

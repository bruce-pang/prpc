package com.brucepang.prpc.metadata.definition.builder;

import com.brucepang.prpc.metadata.definition.TypeDefinitionBuilder;
import com.brucepang.prpc.metadata.definition.model.TypeDefinition;
import com.brucepang.prpc.metadata.definition.util.ClassUtils;
import com.brucepang.prpc.metadata.definition.util.JaketConfigurationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author BrucePang
 */
public final class DefaultTypeBuilder {
    private DefaultTypeBuilder() {}

    public static TypeDefinition build(Class<?> clazz, Map<String, TypeDefinition> typeCache) {
        String className = clazz.getCanonicalName();
        if (className == null) {
            className = clazz.getName();
        }

        // Try to get a cached definition
        TypeDefinition td = typeCache.get(className);
        if (td != null) {
            return td;
        }
        td = new TypeDefinition(className);
        typeCache.put(className, td);

        // Primitive type
        if (!JaketConfigurationUtils.needAnalyzing(clazz)) {
            return td;
        }

        List<Field> fields = ClassUtils.getNonStaticFields(clazz);
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> fieldClass = field.getType();
            Type fieldType = field.getGenericType();
            TypeDefinition fieldTd = TypeDefinitionBuilder.build(fieldType, fieldClass, typeCache);
            td.getProperties().put(fieldName, fieldTd.getType());
        }

        return td;
    }
}

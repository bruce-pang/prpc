package com.brucepang.prpc.metadata.definition;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.metadata.definition.builder.TypeBuilder;
import com.brucepang.prpc.scope.model.GlobalModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author BrucePang
 */
public class TypeDefinitionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TypeDefinitionBuilder.class);
    public static List<TypeBuilder> BUILDERS;

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
}

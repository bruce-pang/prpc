package com.brucepang.prpc.metadata.definition;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.metadata.definition.builder.TypeBuilder;

import java.util.List;

/**
 * @author BrucePang
 */
public class TypeDefinitionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TypeDefinitionBuilder.class);
    public static List<TypeBuilder> BUILDERS;
}

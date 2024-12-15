package com.brucepang.prpc.metadata.definition.model;

import com.brucepang.prpc.common.compiler.support.ClassUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author BrucePang
 */
public class ServiceDefinition implements Serializable {

    /**
     * the canonical name of interface
     *
     * @see Class#getCanonicalName()
     */
    private String canonicalName;

    /**
     * the location of class file
     *
     * @see ClassUtils#getCodeSource(Class)
     */
    private String codeSource;

    private List<MethodDefinition> methods;

    /**
     * the definitions of type
     */
    private List<TypeDefinition> types;
}

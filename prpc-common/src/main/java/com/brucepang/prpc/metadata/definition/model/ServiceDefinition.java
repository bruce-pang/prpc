package com.brucepang.prpc.metadata.definition.model;

import com.brucepang.prpc.common.compiler.support.ClassUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * the definitions of annotations
     */
    private List<String> annotations;

    public List<String> getAnnotations() {
        if (annotations == null) {
            annotations = Collections.emptyList();
        }
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getCodeSource() {
        return codeSource;
    }

    public void setCodeSource(String codeSource) {
        this.codeSource = codeSource;
    }

    public List<MethodDefinition> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDefinition> methods) {
        this.methods = methods;
    }

    public List<TypeDefinition> getTypes() {
        if (types == null) {
            types = new ArrayList<>();
        }
        return types;
    }

    public void setTypes(List<TypeDefinition> types) {
        this.types = types;
    }
}

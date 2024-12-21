package com.brucepang.prpc.metadata.definition.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author BrucePang
 */
public class MethodDefinition implements java.io.Serializable {
    private String name;
    private String[] parameterTypes;
    private String returnType;
    private List<String> annotations;

    public List<String> getAnnotations() {
        if (annotations == null) {
            annotations = new java.util.ArrayList<>();
        }
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "MethodDefinition{" +
                "annotations=" + annotations +
                ", name='" + name + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", returnType='" + returnType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MethodDefinition that = (MethodDefinition) o;
        return Objects.equals(name, that.name) && Objects.deepEquals(parameterTypes, that.parameterTypes) && Objects.equals(returnType, that.returnType) && Objects.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, Arrays.hashCode(parameterTypes), returnType, annotations);
    }
}

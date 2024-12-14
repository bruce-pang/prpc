package com.brucepang.prpc.metadata.definition.model;

import com.brucepang.prpc.metadata.definition.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * @author BrucePang
 */
public class TypeDefinition implements java.io.Serializable {
    /**
     * the name of type
     *
     * @see Class#getCanonicalName()
     * @see ClassUtils#getCanonicalNameForParameterizedType(ParameterizedType)
     */
    private String type;

    /**
     * the items(generic parameter) of Map/List(ParameterizedType)
     * <p>
     * if this type is not ParameterizedType, the items is null or empty
     */
    private List<String> items;

    /**
     * the enum's value
     * <p>
     * If this type is not enum, enums is null or empty
     */
    private List<String> enums;

    /**
     * the key is property name,
     * the value is property's type name
     */
    private Map<String, String> properties;

    public TypeDefinition() {}

    public TypeDefinition(String type) {
        this.setType(type);
    }

    public void setEnums(List<String> enums) {
        this.enums = enums;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getEnums() {
        return enums;
    }

    public List<String> getItems() {
        return items;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getType() {
        return type;
    }
}

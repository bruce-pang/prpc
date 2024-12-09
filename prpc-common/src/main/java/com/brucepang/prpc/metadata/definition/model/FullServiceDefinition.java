package com.brucepang.prpc.metadata.definition.model;

import java.util.Map;

/**
 * FullServiceDefinition
 * @author BrucePang
 */
public class FullServiceDefinition extends ServiceDefinition {

    private Map<String, String> parameters;

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "FullServiceDefinition{" + "parameters=" + parameters + "} " + super.toString();
    }

}

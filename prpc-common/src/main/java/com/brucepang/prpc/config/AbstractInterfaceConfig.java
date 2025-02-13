package com.brucepang.prpc.config;

/**
 * @author BrucePang
 */
public abstract class AbstractInterfaceConfig extends AbstractMethodConfig {
    private static final long serialVersionUID = -1559314110797223229L;

    /**
     * The interface name of the exported service
     */
    protected String interfaceName;
}

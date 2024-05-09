package com.brucepang.prpc.config;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Utility methods and public methods for parsing configuration
 * 用于解析配置而抽取的公共方法
 * @author BrucePang
 */
@Slf4j
public abstract class AbstractConfig implements Serializable {
    private static final long serialVersionUID = 8325242654159435790L;

    /**
     * The suffix container
     */
    private static final String[] SUFFIXES = new String[]{"Config", "Bean", "ConfigBase"};
}

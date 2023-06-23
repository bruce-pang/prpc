package com.brucepang.prpc.spring.service;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author BrucePang
 * 维护对应bean接口与方法的关系
 */
@Data
public class BeanMethod {
    private Object bean;
    private Method method;
}

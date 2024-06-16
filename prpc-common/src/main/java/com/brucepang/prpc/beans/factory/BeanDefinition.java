package com.brucepang.prpc.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanDefinition<T> {
    private Class<T> beanClass;
    private String initMethodName;
    private String destroyMethodName;
    private T beanInstance;

    public BeanDefinition(Class<T> beanClass, String initMethodName, String destroyMethodName) {
        this.beanClass = beanClass;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
        try {
            this.beanInstance = beanClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public T getBeanInstance() {
        return beanInstance;
    }

    public void initialize() {
        invokeMethod(initMethodName);
    }

    public void destroy() {
        invokeMethod(destroyMethodName);
    }

    private void invokeMethod(String methodName) {
        if (methodName != null) {
            try {
                Method method = beanClass.getMethod(methodName);
                method.invoke(beanInstance);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
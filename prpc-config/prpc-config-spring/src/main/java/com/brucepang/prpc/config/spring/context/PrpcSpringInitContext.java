package com.brucepang.prpc.config.spring.context;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

/**
 * Prpc Spring initialization context object
 * @author BrucePang
 */
public class PrpcSpringInitContext {
    private BeanDefinitionRegistry registry;

    private ConfigurableListableBeanFactory beanFactory;

    private ApplicationContext applicationContext;

    private volatile boolean bound;

    public void markAsBound() {
        bound = true;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

package com.brucepang.prpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author BrucePang
 */
@Deprecated
@Component
public class SpringBeanManager implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanManager.applicationContext = applicationContext;
    }
    /**
     * 获取bean实例
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}

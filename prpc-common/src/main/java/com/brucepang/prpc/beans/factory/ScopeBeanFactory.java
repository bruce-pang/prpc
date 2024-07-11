package com.brucepang.prpc.beans.factory;

import com.brucepang.prpc.extension.ExtensionAccessor;
import com.brucepang.prpc.extension.ExtensionPostProcessor;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A bean factory
 *
 * @author BrucePang
 */
public class ScopeBeanFactory {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ScopeBeanFactory.class);

    private Map<String, BeanDefinition<?>> beanDefinitionMap = new ConcurrentHashMap<>();

    private final ScopeBeanFactory parent;

    private final ExtensionAccessor extensionAccessor;

    private final List<ExtensionPostProcessor> extensionPostProcessors;

    public ScopeBeanFactory(ScopeBeanFactory parent, ExtensionAccessor extensionAccessor) {
        this.parent = parent;
        this.extensionAccessor = extensionAccessor;
        extensionPostProcessors = extensionAccessor.getExtensionMgt().getExtensionPostProcessors();
    }

    public <T> T getBean(String name) {
        BeanDefinition<T> beanDefinition = (BeanDefinition<T>) beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is defined");
        }
        return beanDefinition.getBeanInstance();
    }

    public <T> void registerBeanDefinition(String name, BeanDefinition<T> beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

    public void initializeBeans() {
        beanDefinitionMap.values().forEach(BeanDefinition::initialize);
    }

    public void destroyBeans() {
        beanDefinitionMap.values().forEach(BeanDefinition::destroy);
    }

}

package com.brucepang.prpc.beans.factory;

import com.brucepang.prpc.extension.ExtensionAccessor;
import com.brucepang.prpc.extension.ExtensionPostProcessor;
import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.util.StrUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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

    private final List<BeanInfo> registeredBeanInfos = new CopyOnWriteArrayList<>();

    public ScopeBeanFactory(ScopeBeanFactory parent, ExtensionAccessor extensionAccessor) {
        this.parent = parent;
        this.extensionAccessor = extensionAccessor;
        extensionPostProcessors = extensionAccessor.getExtensionMgt().getExtensionPostProcessors();
    }

    public <T> T getBean(String name, Class<T> type) {
        T bean = getBeanInternal(name, type);
        if (bean == null && parent != null) {
            return parent.getBean(name, type);
        }
        return bean;
    }

    public <T> T getBeanInternal(String name, Class<T> type) {
        if (type == Object.class) {
            return null;
        }
        Optional<BeanInfo> matchedBean = registeredBeanInfos.stream()
                .filter(beanInfo -> type.isAssignableFrom(beanInfo.instance.getClass()))
                .filter(beanInfo -> StrUtil.isEquals(beanInfo.name, name))
                .findFirst();

        if (matchedBean.isPresent()) {
            return (T) matchedBean.get().instance;
        }

        // Handling the case where the bean name does not match but there is only one candidate
        List<BeanInfo> candidates = registeredBeanInfos.stream()
                .filter(beanInfo -> type.isAssignableFrom(beanInfo.instance.getClass()))
                .collect(Collectors.toList());

        if (candidates.size() == 1) {
            return (T) candidates.get(0).instance;
        } else if (candidates.size() > 1) {
            List<String> candidateBeanNames = candidates.stream().map(beanInfo -> beanInfo.name).collect(Collectors.toList());
            throw new RuntimeException("expected single matching bean but found " + candidates.size() + " candidates for type [" + type.getName() + "]: " + candidateBeanNames);
        }

        return null;
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

    static class BeanInfo {
        private final String name;
        private final Object instance;

        public BeanInfo(String name, Object instance) {
            this.name = name;
            this.instance = instance;
        }
    }

}

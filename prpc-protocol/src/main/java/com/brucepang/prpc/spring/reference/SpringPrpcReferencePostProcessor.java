package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.annotation.PrpcRemoteReference;
import com.brucepang.prpc.exception.PrpcException;
import com.brucepang.prpc.util.YamlToMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BrucePang
 */
@Slf4j
public class SpringPrpcReferencePostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private ApplicationContext context;
    private ClassLoader classLoader;


    // 保存所有的PrpcRemoteReference注解的创建代理对象的beanDefinition
    private final Map<String, BeanDefinition> prpcRefBeanDefinition = new ConcurrentHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    // spring容器加载了bean的定义文件之后，在bean实例化之前执行的方法
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                // 如果beanClassName与目标类一致，则需要重新定义
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                // 遍历clazz的所有字段，找到带有PrpcRemoteReference注解的字段
                ReflectionUtils.doWithFields(clazz, this::parsePrpcReference);
            }
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.prpcRefBeanDefinition.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)) {
                log.warn("SpringContext already register bean {}", beanName);
                return;
            }
            registry.registerBeanDefinition(beanName, beanDefinition);
            log.info("registered PrpcReferenceBean {} success", beanName);
        });

    }

    private void parsePrpcReference(Field field) {
        PrpcRemoteReference getRemoteReference = AnnotationUtils.getAnnotation(field, PrpcRemoteReference.class);
        if (getRemoteReference != null) {
            // 需要注入一个实例
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringPrpcReferenceBean.class);
            Map<String, Object> map = null;
            try {
                map = YamlToMap.yamlToMap("application.yml");
            } catch (FileNotFoundException | NullPointerException e) {
                throw new PrpcException(2000000006,"application.yml not found");
            }
            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceAddress", map.get("com.brucepang.prpc.address"));
            builder.addPropertyValue("servicePort", map.get("com.brucepang.prpc.port"));

            BeanDefinition beanDefinition = builder.getBeanDefinition();
            prpcRefBeanDefinition.put(field.getName(), beanDefinition);
        }
    }
}

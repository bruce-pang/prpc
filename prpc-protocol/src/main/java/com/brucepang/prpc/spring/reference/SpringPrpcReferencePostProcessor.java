package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.annotation.PrpcRemoteReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.StringUtils;

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
    private PrpcClientProperties prpcClientProperties;

    public SpringPrpcReferencePostProcessor(PrpcClientProperties prpcClientProperties) {
        this.prpcClientProperties = prpcClientProperties;
    }


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
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames() ){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                // 如果beanClassName与目标类一致，则需要重新定义
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                // 遍历clazz的所有字段，找到带有PrpcRemoteReference注解的字段
                ReflectionUtils.doWithFields(clazz,this::parsePrpcReference);
            }
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.prpcRefBeanDefinition.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)){
                log.warn("SpringContext already register bean {}", beanName);
                return;
            }
            registry.registerBeanDefinition(beanName, beanDefinition);
            log.info("registered PrpcReferenceBean {} success", beanName);
        });

    }

    /**
     * 解析PrpcRemoteReference注解，将被注解的字段的beanDefinition注册到spring容器中
     * @param field
     */
    private void parsePrpcReference(Field field){
        PrpcRemoteReference getRemoteReference = AnnotationUtils.getAnnotation(field, PrpcRemoteReference.class);
        if (getRemoteReference != null){
            // 需要注入一个实例
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringPrpcReferenceBean.class);
            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass", field.getType());
            if (!StringUtils.isEmpty(prpcClientProperties.getServiceAddress())) {
                builder.addPropertyValue("serviceAddress", prpcClientProperties.getServiceAddress());
                builder.addPropertyValue("servicePort", prpcClientProperties.getServicePort());
            } else {
                builder.addPropertyValue("registryAddress",prpcClientProperties.getRegistryAddress());
                builder.addPropertyValue("registryType",prpcClientProperties.getRegistryType());
            }

            BeanDefinition beanDefinition = builder.getBeanDefinition();
            prpcRefBeanDefinition.put(field.getName(), beanDefinition);
        }
    }
}

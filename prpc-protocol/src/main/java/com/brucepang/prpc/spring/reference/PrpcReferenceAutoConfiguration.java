package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.annotation.PrpcRemoteReference;
import com.brucepang.prpc.spring.service.PrpcProviderAutoConfiguration;
import com.brucepang.prpc.spring.service.PrpcServerProperties;
import com.brucepang.prpc.spring.service.SpringPrpcProviderBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author BrucePang
 * spring容器启动时获取配置文件中的配置信息并启动Netty服务，以及将标记了PrpcRemoteService注解的bean注册到spring容器中
 */
@Configuration
@ConditionalOnClass({PrpcRemoteReference.class}) // 项目中要存在指定的类 自动装配才生效
@EnableAutoConfiguration(exclude = PrpcProviderAutoConfiguration.class)
// 由于Spring执行顺序问题，这里的EnableConfigurationProperties注解无用，BeanFactoryPostProcessor在执行之前就得拿到配置文件中的配置信息，所以这里使用EnvironmentAware
public class PrpcReferenceAutoConfiguration implements EnvironmentAware {

    private Environment environment;



    @Bean
   @ConditionalOnMissingBean(name = "postProcessor") // 当容器里没有指定的Bean的情况下创建该对象
    public SpringPrpcReferencePostProcessor postProcessor(){
        PrpcClientProperties rc = new PrpcClientProperties();
        if (!StringUtils.isEmpty(this.environment.getProperty("com.brucepang.prpc.client.serviceAddress"))){
            rc.setServiceAddress(this.environment.getProperty("com.brucepang.prpc.client.serviceAddress"));
            int port = Integer.parseInt(this.environment.getProperty("com.brucepang.prpc.client.servicePort"));
            rc.setServicePort(port);
        } else {
            rc.setRegistryAddress(this.environment.getProperty("com.brucepang.prpc.client.registryAddress"));
            rc.setRegistryType(Byte.parseByte(this.environment.getProperty("com.brucepang.prpc.client.registryType")));
        }
        return new SpringPrpcReferencePostProcessor(rc);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

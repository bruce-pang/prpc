package com.brucepang.prpc.spring.service;

import com.brucepang.prpc.registry.IRegistryService;
import com.brucepang.prpc.registry.RegistryType;
import com.brucepang.prpc.spring.reference.PrpcReferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.brucepang.prpc.registry.RegistryFactory;
import org.springframework.util.Assert;

import java.net.UnknownHostException;

/**
 * @author BrucePang
 * spring容器启动时获取配置文件中的配置信息并启动Netty服务，以及将标记了PrpcRemoteService注解的bean注册到spring容器中
 */
@Configuration
@EnableConfigurationProperties(PrpcServerProperties.class)
@EnableAutoConfiguration(exclude = PrpcReferenceAutoConfiguration .class) // 扫描到PrpcReferenceAutoConfiguration类时，不自动配置
public class PrpcProviderAutoConfiguration {

    @Bean
    public SpringPrpcProviderBean springPrpcProviderBean(PrpcServerProperties prpcServerProperties) throws UnknownHostException {
        // TODO 本地调用与远程调用的bug
        if (prpcServerProperties.getServiceAddress() == null || "".equals(prpcServerProperties.getServiceAddress().trim())) {
            Assert.notNull(prpcServerProperties.getRegistryAddress(), "registryAddress must not be null");
            byte registryType = prpcServerProperties.getRegistryType();
            if (RegistryType.NACOS.code() == prpcServerProperties.getRegistryType()) { // 如果是nacos注册中心
                return new SpringPrpcProviderBean(prpcServerProperties);
            } else if (RegistryType.ZOOKEEPER.code() == prpcServerProperties.getRegistryType()) { // 如果是zookeeper注册中心
                IRegistryService registryService = RegistryFactory.createRegistry(prpcServerProperties.getRegistryAddress(), RegistryType.findByCode(prpcServerProperties.getRegistryType()));
                return new SpringPrpcProviderBean(prpcServerProperties);
            } else if (RegistryType.EUREKA.code() == prpcServerProperties.getRegistryType()) { // 如果是EUREKA注册中心
                /*IRegistryService registryService = RegistryFactory.createRegistry(prpcServerProperties.getServiceAddress(), RegistryType.findByCode(prpcServerProperties.getRegistryType()));
                return new SpringPrpcProviderBean(prpcServerProperties.getServiceAddress(), prpcServerProperties.getServicePort(), registryService);*/
                throw new RuntimeException("Eureka Registry will support later versions");
            } else {
                throw new RuntimeException("registryType is not support");
            }
        } else{
            // 本地调用
            return new SpringPrpcProviderBean(prpcServerProperties.getServiceAddress(),prpcServerProperties.getServicePort(),null);
        }

    }
}

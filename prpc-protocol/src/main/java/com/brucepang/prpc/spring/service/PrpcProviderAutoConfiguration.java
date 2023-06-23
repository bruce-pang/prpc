package com.brucepang.prpc.spring.service;

import com.brucepang.prpc.registry.IRegistryService;
import com.brucepang.prpc.registry.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.brucepang.prpc.registry.RegistryFactory;

import java.net.UnknownHostException;

/**
 * @author BrucePang
 * spring容器启动时获取配置文件中的配置信息并启动Netty服务，以及将标记了PrpcRemoteService注解的bean注册到spring容器中
 */
@Configuration
@EnableConfigurationProperties(PrpcServerProperties.class)
public class PrpcProviderAutoConfiguration {

    @Bean
    public SpringPrpcProviderBean springPrpcProviderBean(PrpcServerProperties prpcServerProperties) throws UnknownHostException {

        if (prpcServerProperties.getServiceAddress() == null || "".equals(prpcServerProperties.getServiceAddress().trim())) {
            if (RegistryType.NACOS.code() == prpcServerProperties.getRegistryType()) {
                return new SpringPrpcProviderBean(prpcServerProperties);
            } else {
                return new SpringPrpcProviderBean(prpcServerProperties.getServicePort());
            }
        }
        IRegistryService registryService = RegistryFactory.createRegistry(prpcServerProperties.getServiceAddress(), RegistryType.findByCode(prpcServerProperties.getRegistryType()));
        return new SpringPrpcProviderBean(prpcServerProperties.getServiceAddress(), prpcServerProperties.getServicePort(), registryService);
    }
}

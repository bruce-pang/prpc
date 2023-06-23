package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.spring.service.PrpcServerProperties;
import com.brucepang.prpc.spring.service.SpringPrpcProviderBean;
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
public class PrpcReferenceAutoConfiguration implements EnvironmentAware {
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    @Bean
    public SpringPrpcReferencePostProcessor postProcessor(){
        PrpcClientProperties rc=new PrpcClientProperties();
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

}

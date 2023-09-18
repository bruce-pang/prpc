package com.brucepang.prpc.spring.config;


import com.brucepang.prpc.common.IRegistryService;
import com.brucepang.prpc.spring.facade.RegistryFacade;
import com.brucepang.prpc.spring.listener.DefaultPrpcListener;
import com.brucepang.prpc.spring.properties.PrpcProperties;
import com.brucepang.prpc.spring.reference.SpringPrpcReferencePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author BrucePang
 */
@Configuration
public class PrpcAutoConfiguration {

    // 监听器
    @Bean
    public DefaultPrpcListener defaultRpcListener(
            @Autowired IRegistryService iRegistryService,
            @Autowired PrpcProperties prpcProperties
                                                  ) {
        return new DefaultPrpcListener(iRegistryService, prpcProperties);
    }

    // 配置属性
    @Bean
    @Order(1)
    public PrpcProperties rpcProperty() {
        return new PrpcProperties();
    }


    @Bean
    public IRegistryService registryService(@Autowired PrpcProperties rpcProperties) {
        return RegistryFacade.registrySelector(rpcProperties);
    }


    @Bean
    public SpringPrpcReferencePostProcessor springPrpcReferencePostProcessor(){
        return new SpringPrpcReferencePostProcessor();
    }



}

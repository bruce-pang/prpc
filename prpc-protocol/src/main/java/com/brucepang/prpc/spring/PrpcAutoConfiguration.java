package com.brucepang.prpc.spring;

import com.brucepang.prpc.spring.reference.PrpcClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.brucepang.prpc.spring.reference.SpringPrpcReferencePostProcessor;

/**
 * @author BrucePang
 */
//@Configuration
//@ConditionalOnClass({SpringPrpcReferencePostProcessor.class}) // 项目中要存在指定的类 自动装配才生效
//@EnableConfigurationProperties(PrpcClientProperties.class) // 开启属性注入,通过@autowired注入
public class PrpcAutoConfiguration {
}

package com.brucepang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 */
// 此处有bug，扫包需要特别注意，不要扫到客户端的包了，不然会报错
@SpringBootApplication
@ComponentScan(basePackages = { "com.brucepang.prpc.annotation", "com.brucepang.prpc.provider.example.service"}
        /*,excludeFilters = {
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {PrpcClientProperties.class}),
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {PrpcInvokerProxy.class}),
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {PrpcReferenceAutoConfiguration.class}),
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {SpringPrpcReferenceBean.class}),
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {SpringPrpcReferencePostProcessor.class}),
    }*/
)
public class PrpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrpcProviderApplication.class, args);
//        new NettyServer("127.0.0.1", 8080).startNettyServer();
    }
}

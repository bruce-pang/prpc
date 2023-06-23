package com.brucepang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
// 此处有bug，扫包需要特别注意，不要扫到客户端的包了，不然会报错
@SpringBootApplication(scanBasePackages = {"com.brucepang.prpc.spring.service","com.brucepang.prpc.annotation","com.brucepang.prpc.provider.example.service"})
public class PrpcProviderApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(PrpcProviderApplication.class, args);
//        new NettyServer("127.0.0.1", 8080).startNettyServer();
    }
}

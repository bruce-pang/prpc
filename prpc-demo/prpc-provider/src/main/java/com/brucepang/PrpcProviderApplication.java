package com.brucepang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
// 此处有bug，扫包需要特别注意，不要扫到客户端的包了，不然会报错
@SpringBootApplication
public class PrpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrpcProviderApplication.class, args);
    }
}

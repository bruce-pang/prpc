package com.brucepang.prpc.consumer.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author BrucePang
 */

@SpringBootApplication
public class PrpcConsumerApplication {
    public static void main(String[] args) {

        SpringApplication.run(PrpcConsumerApplication.class, args);
    }
}

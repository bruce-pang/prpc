package com.brucepang;

import com.brucepang.prpc.protocol.NettyServer;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = "com.brucepang")
public class PrpcProvider
{
    public static void main( String[] args )
    {
        SpringApplication.run(PrpcProvider.class, args);
        new NettyServer("127.0.0.1", 8080).startNettyServer();
    }
}

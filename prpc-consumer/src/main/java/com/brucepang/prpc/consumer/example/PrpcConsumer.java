package com.brucepang.prpc.consumer.example;

import com.brucepang.prpc.api.example.IPersonService;

/**
 * Hello world!
 *
 */
//@SpringBootApplication(scanBasePackages = "com.brucepang")
public class PrpcConsumer
{
    public static void main( String[] args )
    {
        RpcClientProxy rpc = new RpcClientProxy();
        IPersonService person = rpc.clientProxy(IPersonService.class, "127.0.0.1", 8080);
        System.out.println(person.save("bruce"));
    }
}

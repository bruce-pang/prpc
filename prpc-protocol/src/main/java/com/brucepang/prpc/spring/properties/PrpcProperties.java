package com.brucepang.prpc.spring.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * @author BrucePang
 */
@ConfigurationProperties(prefix = "com.brucepang.prpc") // 声明当前类是一个Properties类，定义在全局配置文件中的前缀
public class PrpcProperties { // Spring的properties配置类不要继承java.util.Properties，否则会造成当前类的优先级大于配置文件的优先级
    private String address = "nacos://127.0.0.1:8848"; // 服务提供方地址,默认使用nacos为注册中心
    private int port = 20880; // 服务提供方运行占用端口

    private String applicationName = "prpc-demo"; // 服务名称；


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

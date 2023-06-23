# pRPC
目前发布第一版：

## 介绍
该框架是一个采用Netty实现的Rpc框架，适用于Spring Boot项目，计划更新支持SpringCloud，SpringCloudAlibaba。 目前支持的版本为Spring Boot 2.5.x。核心代码模块都有清晰的注解，主要是用于学习之用。

### 工程结构
![image](https://github.com/bruce-pang/pRPC/assets/125526597/c06744ac-0a12-4520-998e-1fd309983201)


## 安装教程

在pom文件中加上依赖：
```xml
            <dependency>
                <groupId>com.brucepang.prpc</groupId>
                <artifactId>prpc-protocol</artifactId>
                <version>1.2.0</version>
            </dependency>
```

## 使用说明
该工程中，prpc-protocol为prpc框架本身，可以导出jar包作为使用；
其余子工程分别对应为demo：
    prpc-api ---> demo：公共代码
    prpc-provider ---> demo：服务提供方
    prpc-consumer ---> demo：服务调用方

目前仅支持本地启动。正在加速更新整合nacos，添加负载均衡。


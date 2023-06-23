# pRPC
目前发布第一版：

## 介绍
该框架是一个采用Netty实现的Rpc框架，适用于Spring Boot项目，计划更新支持SpringCloud，SpringCloudAlibaba。 目前支持的版本为Spring Boot 2.5.x。核心代码模块都有清晰的注解，主要是用于学习之用。

### 开发环境介绍
    JDK：1.8
    Spring Boot：2.5.3
    Netty：4.1.66.Final
    zookeeper：3.4.10
    nacos：1.3.0
    lombok：1.16.18

### 工程结构
![image](https://github.com/bruce-pang/pRPC/assets/125526597/c06744ac-0a12-4520-998e-1fd309983201)


## 安装教程

在pom文件中加上依赖：
```xml
            <dependency>
                <groupId>com.brucepang.prpc</groupId>
                <artifactId>prpc-protocol</artifactId>
                <version>1.3.0</version>
            </dependency>
```

## 使用说明
该工程中:

    prpc-protocol为prpc框架本身，可以导出jar包作为使用；
    prpc-registry为服务发现工程，目前支持zookeeper和nacos；

其余子工程分别对应为demo：

    prpc-protocol ---> 框架本身
    prpc-registry ---> 注册中心与服务发现
    prpc-api ---> demo：公共代码
    prpc-provider ---> demo：服务提供方
    prpc-consumer ---> demo：服务调用方
    
快速体验【纯体验，不写代码篇】：

    1.打开zookeeper或nacos
        注意：
         1.1 zookeeper本项目采用的是3.4.10版本，如果使用3.5.x版本，需要修改prpc-registry pom.xml中的curator-version为3.x.x以上版本,以下为项目中的配置：
```xml
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!--
            Curator 2.x.x - compatible with both ZooKeeper 3.4.x and ZooKeeper 3.5.x
            Curator 3.x.x - compatible only with ZooKeeper 3.5.x and includes support for new features such as dynamic reconfiguration, etc.
        -->
        <curator-version>2.13.0</curator-version>
    </properties>
```
    2.打开prpc-provider工程，配置application.yml，如下是参数配置：
```yaml
com:
  brucepang:
    prpc:
      server:
        servicePort: 20880 # 服务端口【本地调用使用】
        registryType: 0 # 注册中心类型 0：zookeeper 1：eureka 2：nacos
        registryAddress: 192.168.56.1:2181 # 注册中心地址 zookeeper默认端口2181， eureka默认端口8761， nacos默认端口8848
```
    3.若不需要编写业务代码，直接运行PrpcProviderApplication.java，即可启动服务提供方。
    4.打开prpc-consumer工程，配置application.yml，如下是参数配置：
```yaml
com:
  brucepang:
    prpc:
      client:
        enableRegistry: false # 是否启用注册中心, 默认true
        serviceAddress: 192.168.56.1 # 服务地址【本地调用使用】
        servicePort: 20880 # 服务端口【本地调用使用】
#         registryAddress: 192.168.56.1:8848 # 注册中心地址 zookeeper默认端口2181， eureka默认端口8761， nacos默认端口8848
#         registryType: 2 # 注册中心类型 0：zookeeper 1：eureka 2：nacos
```
    5.若不需要编写业务代码，直接运行PrpcConsumerApplication.java，即可启动服务调用方。
    6.启动完成后，
    访问http://localhost:8080/hello?name=BrucePang，您能够看到：

    访问http://localhost:8080/fire?name=刘三石，您能够看到：
    
框架目前只是功能基础实现版，尚有很多优化点，
例如：

    1.目前只支持zookeeper和nacos，后续会支持eureka；
    2.反射调用使用的是jdk动态代理，性能远远比不上asm字节码生成，后续会支持asm字节码生成；
    3.服务列表目前每次都是远程获取的，尚未做本地缓存，后续会支持本地缓存
    4.打包成springboot-starter，方便使用；
    5.序列化使用jdk自带的序列化，性能远远比不上msgpack，后续会支持msgpack；
    6.为了实现切换本地调用和远程调用，目前使用了有比较多冗余代码没有采用合适的设计模式，后续会优化；
    ....
本版本总结：

    1.基于netty实现了基本的rpc功能；
    2.期间对于spring生命周期的理解更加深刻，并且对于spring的底层组件使用场景有了更深刻的印象；
    3.实现了最基本的负载均衡，目前只支持随机负载均衡；

### 天涯何处无芳草，给颗星星好不好┭┮﹏┭┮]

### 后续会持续更新，欢迎大家提出宝贵意见，一起学习，一起进步。





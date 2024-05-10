<h1 align="center">prpc</h1>
<p align="center">
  <strong>基于Netty</strong>
</p>

<h5 align="center"> 

[English](README.md) | 简体中文 

</h5>

## 📚简介
该框架是一个采用Netty实现的RPC框架，适用于Spring Boot项目，计划更新支持SpringCloud，SpringCloudAlibaba。 核心代码模块都有清晰的注解，主要是用于学习之用。

### 开发环境介绍
    JDK：1.8
    maven：3.8.6
    Spring Boot：2.5.6
    Netty：4.1.66.Final
    zookeeper：3.4.10
    nacos：1.3.0
    lombok：1.16.18
    fastjson：1.2.76

### 工程结构
![image](https://github.com/bruce-pang/pRPC/assets/125526597/74f6c101-df52-4328-ae7c-e8b4e5cd20d0)




## 安装教程

在pom文件中加上依赖：
```xml
            <dependency>
                <groupId>com.brucepang.prpc</groupId>
                <artifactId>prpc-protocol</artifactId>
                <version>1.4.1</version>
            </dependency>
```

## 使用说明
该工程中:
    
    prpc-common为框架公共组件部分，存放后续的诸多配置类与工具类
    prpc-protocol为prpc框架本身，可以导出jar包作为使用；
    prpc-registry为服务发现工程，目前支持zookeeper和nacos；

其余子工程分别对应为demo：
    
    prpc-common     ---> 公共组件
    prpc-demo       ---> 示例代码（下面三部分组成）
      prpc-api      ---> 存放公共接口部分
      prpc-consumer ---> 服务消费方
      prpc-provider ---> 服务提供方
    prpc-protocol   ---> 框架本身
    prpc-registry   ---> 注册中心与服务发现
    
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
      applicationName: prpc-provider
      port: 20880 # 服务端口【prpc启动端口号】
      address: nacos://127.0.0.1:8848 # 注册中心地址 zookeeper默认端口2181， eureka默认端口8761， nacos默认端口8848
```
    3.若不需要编写业务代码，直接运行PrpcProviderApplication.java，即可启动服务提供方。
    4.打开prpc-consumer工程，配置application.yml，如下是参数配置：
```yaml
com:
  brucepang:
    prpc:
      applicationName: prpc-provider
      port: 20880 # 服务端口【prpc启动端口号】
      address: nacos://127.0.0.1:8848 # 注册中心地址 zookeeper默认端口2181， eureka默认端口8761， nacos默认端口8848
```
    5.若不需要编写业务代码，直接运行PrpcConsumerApplication.java，即可启动服务调用方。

    
6.启动完成后，
    访问http://localhost:8080/hello?name=BrucePang，您能够看到：
    
![image](https://github.com/bruce-pang/pRPC/assets/125526597/be4768f0-ad3c-49d7-9069-d7770a47578f)

    访问http://localhost:8080/fire?name=刘三石，您能够看到：
    
    
![image](https://github.com/bruce-pang/pRPC/assets/125526597/51ea4a6b-b5b8-4791-aace-98fb16fdafea)

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
1.4.1优化点：
    
    1.解决被打成jar包启动时出现循环依赖问题；
    2.服务提供方接口注册时机实现采用 懒汉式 实现；
    3.服务提供方与服务消费方yml配置统一，不再显示区分提供方与消费方；
    4.不再提供regitryType来区分注册中心，改为address地址区分；
1.4.2 SPI扩展插件:
    

### 天涯何处无芳草，给颗星星好不好┭┮﹏┭┮]

### 后续会持续更新，欢迎大家提出宝贵意见，一起学习，一起进步。





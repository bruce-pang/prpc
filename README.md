<h1 align="center">prpc</h1>
<p align="center">
  <strong>based on netty</strong>
</p>

English | [简体中文](README-CN.md)

## 📚Introduction
The framework is an RPC framework implemented by Netty, which is suitable for Spring Boot projects, and plans to update to support SpringCloud, SpringCloudAlibaba. The core code modules are clearly annotated and are mainly used for learning purposes.

### Environmental introduction
    JDK：1.8
    maven：3.8.6
    Spring Boot：2.5.6
    Netty：4.1.66.Final
    zookeeper：3.4.10
    nacos：1.3.0
    lombok：1.16.18
    fastjson：1.2.76

### Engineering structures
![image](https://github.com/bruce-pang/pRPC/assets/125526597/74f6c101-df52-4328-ae7c-e8b4e5cd20d0)




## Installation tutorial

add dependencies to the pom file
```xml
            <dependency>
                <groupId>com.brucepang.prpc</groupId>
                <artifactId>prpc-protocol</artifactId>
                <version>1.4.1</version>
            </dependency>
```

## DirectionsForUse
in this project:
    
    prpc-common is the public component part of the framework, which stores many subsequent configuration classes and utility classes
    prpc-protocol is the prpc framework itself, which can export jar packages for use;
    prpc-registry is a service discovery project that currently supports zookeeper and nacos.

The rest of the sub-projects correspond to the demo:
    
    prpc-common     ---> public components
    prpc-demo       ---> sample（the following three parts consist）
      prpc-api      ---> stores the common interface section
      prpc-consumer ---> consumers of services
      prpc-provider ---> service providers
    prpc-protocol   ---> the frame itself
    prpc-registry   ---> registry service discovery
    
Quick experience [pure experience, no code writing]：

    1.start zookeeper or nacos
        warning：
         If you use 3.5.x, you need to change the curator-version in the prpc-registry pom.xml to 3.x.x or later, and the following configuration is in the project:
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
    2.open prpc-provider project, configure application.yml, The following table describes the parameter settings：
```yaml
com:
  brucepang:
    prpc:
      applicationName: prpc-provider
      port: 20880 # Service port【prpc boot port】
      address: nacos://127.0.0.1:8848 # address of the registry zookeeper default port 2181， eureka default port 8761， nacos default port 8848
```
    3.If you do not need to write business code, you can run the Prpc Provider Application.java directly to start the service provider.
    4.Open the prpc-consumer project and configure application.yml. The following is the parameter configuration:
```yaml
com:
  brucepang:
    prpc:
      applicationName: prpc-provider
      port: 20880 # Service port【prpc boot port】
      address: nacos://127.0.0.1:8848 # address of the registry zookeeper default port 2181， eureka default port 8761， nacos default port 8848
```
    5.If there is no need to write business code, just run PrpcConsumerApplication.java directly to start the service caller.

    
6.After the startup is completed,
    Visit http://localhost:8080/hello?name=BrucePang, you can see:
    
![image](https://github.com/bruce-pang/pRPC/assets/125526597/be4768f0-ad3c-49d7-9069-d7770a47578f)

    Visit http://localhost:8080/fire?name=刘三石，you can see:
    
    
![image](https://github.com/bruce-pang/pRPC/assets/125526597/51ea4a6b-b5b8-4791-aace-98fb16fdafea)

The framework is currently only a basic implementation of functionality with many optimization points remaining. 
For example:

    1. Currently, only Zookeeper and Nacos are supported, and Eureka will be supported in the future;
    2. Reflection invocation currently uses JDK dynamic proxy, which is far less performant than ASM bytecode generation, and ASM bytecode generation will be supported in the future;
    3. The service list is currently fetched remotely every time, and local caching has not been implemented yet, but local caching will be supported in the future;
    4. It will be packaged as a SpringBoot Starter for easy use;
    5. The serialization currently uses the built-in JDK serialization, which is far less performant than MessagePack, and MessagePack will be supported in the future;
    6. In order to implement the switch between local and remote calls, there is currently a lot of redundant code that does not use a suitable design pattern, which will be optimized in the future;
    ...

Summary of this version:

    1. Basic RPC functionality has been implemented based on Netty;
    2. During the process, a deeper understanding of the Spring lifecycle has been gained, and a deeper impression of the usage scenarios of Spring's underlying components has been formed;
    3. The most basic load balancing has been implemented, currently only supporting random load balancing;

1.4.1 optimization points:

    1. Resolve the circular dependency issue when starting as a JAR package;
    2. Implement the lazy registration of service provider interfaces on the provider side;
    3. Unify the YML configuration for service providers and consumers, no longer explicitly distinguishing between providers and consumers;
    4. No longer provide regitryType to distinguish between registration centers, use the address to distinguish instead;

1.4.2 SPI extension plugin:

### How to participate in PRPC open source contribution

    1. Fork the project to your own warehouse
    2. Clone the project to your local
    3. Modify the code and submit it to your own warehouse
    4. Submit a pull request to the original project

>Add 'WeChat brucepang0618' to the PRPC to communicate with the author

> Illegal projects are not allowed to be used at your own risk

### Wherever you go, there will always be beautiful scenery. How about giving me a little star?┭┮﹏┭┮]

### The project will be continuously updated. Welcome everyone to give valuable suggestions. Let's learn and progress together.





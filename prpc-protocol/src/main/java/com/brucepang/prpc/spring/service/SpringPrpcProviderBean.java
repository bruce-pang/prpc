package com.brucepang.prpc.spring.service;

import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.protocol.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author BrucePang
 */
@Slf4j
public class SpringPrpcProviderBean implements InitializingBean, BeanPostProcessor {
    // 主机地址
    private final String serverAddress;
    // 端口号
    private final int serverPort;

    public SpringPrpcProviderBean(int serverPort) throws UnknownHostException {
        this.serverPort = serverPort;
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        log.info("serverAddress: {}", serverAddress);
    }

    public SpringPrpcProviderBean(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("begin deploy Netty Server to host: {}, port: {}", serverAddress, serverPort);
        new Thread(() -> {
            try {
                new NettyServer(serverAddress, serverPort).startNettyServer();
            } catch (Exception e) {
                log.error("deploy Netty Server to host: {}, port: {} failed", serverAddress, serverPort, e);
            }
        }).start();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    /**
     * 在bean初始化之后，将作为提供服务的bean的名字与方法的映射关系保存到map集合中，以便于请求时能找到对应关系
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 只要bean声明了PrpcRemoteService注解，就需要把bean作为服务发送到网络上
        if(bean.getClass().isAnnotationPresent(PrpcRemoteService.class)){
            log.info("deploy remote service: {}", bean.getClass().getName());
            // 获取bean的所有方法
            Method[] methods = bean.getClass().getDeclaredMethods();
            //
            for (Method method : methods) {
                // 具体发布的bean的映射关系保存下来(保存到map集合中)【该类实现的第一个接口的名字.方法名】
                String key = bean.getClass().getInterfaces()[0].getName() + "." + method.getName();
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.beanMethodMap.put(key, beanMethod);
            }
        }
        return bean;
    }
}

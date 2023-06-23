package com.brucepang.prpc.spring.reference;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author BrucePang
 * 消费端动态代理工厂Bean
 */
public class SpringPrpcReferenceBean implements FactoryBean<Object> {

    private Object object;
    private String serviceAddress;
    private int servicePort;
    private Class<?> interfaceClass;

    public void init(){
        this.object = Proxy.newProxyInstance(SpringPrpcReferenceBean.class.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new PrpcInvokerProxy(serviceAddress, servicePort));
    }

    @Override
    public Object getObject() throws Exception {

        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}

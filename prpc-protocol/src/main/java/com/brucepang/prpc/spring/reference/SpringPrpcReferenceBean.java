package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.registry.IRegistryService;
import com.brucepang.prpc.registry.RegistryFactory;
import com.brucepang.prpc.registry.RegistryType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;

/**
 * @author BrucePang
 * 消费端动态代理工厂Bean
 */
public class SpringPrpcReferenceBean implements FactoryBean<Object>, EnvironmentAware {
    private Environment environment;

    private Object object;
    private String serviceAddress;
    private int servicePort;
    private Class<?> interfaceClass;

    private String registryAddress;

    private byte registryType;

    public void init(){
        String flagStr = this.environment.getProperty("com.brucepang.prpc.client.enableRegistry");
        if (StringUtils.isEmpty(flagStr) || Boolean.parseBoolean(flagStr)) { // 使用注册中心
            IRegistryService registryService = RegistryFactory.createRegistry(this.registryAddress, RegistryType.findByCode(this.registryType));
            this.object = Proxy.newProxyInstance(SpringPrpcReferenceBean.class.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new PrpcInvokerProxy(registryService));
        } else if (!Boolean.parseBoolean(flagStr)) { // 本地调用
            this.object = Proxy.newProxyInstance(SpringPrpcReferenceBean.class.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new PrpcInvokerProxy(this.serviceAddress, this.servicePort, Boolean.parseBoolean(flagStr)));
        } else {
            throw new RuntimeException("com.brucepang.prpc.client.enableRegistry must be true or false");
        }

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

    public void setObject(Object object) {
        this.object = object;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void setRegistryType(byte registryType) {
        this.registryType = registryType;
    }


    public String getRegistryAddress() {
        return registryAddress;
    }

    public byte getRegistryType() {
        return registryType;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}

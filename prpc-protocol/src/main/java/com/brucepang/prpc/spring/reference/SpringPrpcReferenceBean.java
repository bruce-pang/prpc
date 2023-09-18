package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.util.YamlToMap;
import com.brucepang.prpc.common.IRegistryService;
import com.brucepang.prpc.spring.facade.RegistryFacade;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.util.Map;


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


//    private IRegistryService iRegistryService;


    public void init(){
        try {
            Map<String, Object> map = YamlToMap.yamlToMap("application.yml");
            IRegistryService registryService = RegistryFacade.registrySelector(map);
            String address = registryService == null ? InetAddress.getByName("localhost").getHostAddress() : RegistryFacade.getRegistryHostAndPort((String) map.get("com.brucepang.prpc.address")).getHost();
            int port = registryService == null ? 20880 : (int) map.get("com.brucepang.prpc.port");
            if (registryService != null){
                this.object = Proxy.newProxyInstance(SpringPrpcReferenceBean.class.getClassLoader(),
                        new Class<?>[]{interfaceClass},
                        new PrpcInvokerProxy(address,port,registryService,true));
            } else { // 本地调用
                this.object = Proxy.newProxyInstance(SpringPrpcReferenceBean.class.getClassLoader(),
                        new Class<?>[]{interfaceClass},
                        new PrpcInvokerProxy(address,port,false));

            }


        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}

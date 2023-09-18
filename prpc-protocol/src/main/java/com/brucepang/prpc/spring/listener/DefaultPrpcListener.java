package com.brucepang.prpc.spring.listener;

import com.brucepang.prpc.annotation.PrpcRemoteReference;
import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.util.HostAndPort;
import com.brucepang.prpc.util.IPAddressConverter;
import com.brucepang.prpc.handler.reflect.BeanMethod;
import com.brucepang.prpc.handler.reflect.Mediator;
import com.brucepang.prpc.protocol.NettyServer;
import com.brucepang.prpc.common.IRegistryService;
import com.brucepang.prpc.common.ServiceInfo;
import com.brucepang.prpc.spring.properties.PrpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.brucepang.prpc.spring.facade.RegistryFacade.getRegistryHostAndPort;

/**
 * @author BrucePang
 */
@Slf4j
public class DefaultPrpcListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IRegistryService registryService;

    private PrpcProperties prpcProperties;

    private Environment environment;


    public DefaultPrpcListener(IRegistryService registryService, PrpcProperties prpcProperties) {
        this.registryService = registryService;
        this.prpcProperties = prpcProperties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final ApplicationContext applicationContext = event.getApplicationContext();
        // 如果是root application context就开始执行
        if (applicationContext.getParent() == null) {
            // 启动 prpc 服务端
            startPrpcServer(applicationContext);

            // 注入依赖的远端rpc服务
//            injectDependencyService(applicationContext);
        }
    }

    private void startPrpcServer(ApplicationContext applicationContext) {
        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(PrpcRemoteService.class);
        if (beans.size() == 0) {
            return;
        }
        String ip = null;
        final Integer port = prpcProperties.getPort();
        for (Object bean : beans.values()) {
            // final Class<?> clazz = obj.getClass();
            // final Class<?>[] interfaces = clazz.getInterfaces();
            // // TODO: 这里只能应对一个接口, 若类实现了多个接口怎么处理呢?
            // final Class<?> interfaceClazz = interfaces[0];
            // final String name = interfaceClazz.getName(); // 以接口类为服务发现名

            log.info("deploy remote service: {}", bean.getClass().getName());
            // 获取bean的所有方法
            Method[] methods = bean.getClass().getDeclaredMethods();
            //
            for (Method method : methods) {
                String serviceName = bean.getClass().getInterfaces()[0].getName();
                // 具体发布的bean的映射关系保存下来(保存到map集合中)【该类实现的第一个接口的名字.方法名】
                String key = bean.getClass().getInterfaces()[0].getName() + "." + method.getName();
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.beanMethodMap.put(key, beanMethod);
                ip = "127.0.0.1";
                try {
                    HostAndPort hostAndPort = getRegistryHostAndPort(prpcProperties.getAddress());
                    ip = IPAddressConverter.convertToLocalhostIfNecessary(hostAndPort.getHost());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final ServiceInfo serviceInfo = new ServiceInfo(
                        serviceName, // 服务提供者名称
                        ip, // 服务提供者的主机ip
                        port // 服务提供者运行的端口号
                );

                try {
                    // 注册服务
                    registryService.register(serviceInfo);
                } catch (Exception e) {
                    log.error("Fail to register service: {}", e.getMessage());
                }
            }
        }

            // 启动 rpc 服务器，开始监听端口
            log.info("begin deploy Netty Server to host: {}, port: {}", ip, port);
            String finalIp = ip;
            new Thread(() -> {
                try {
                    new NettyServer(finalIp, port).startNettyServer();
                } catch (Exception e) {
                    log.error("deploy Netty Server to host: {}, port: {} failed", finalIp, port, e);
                }
            }).start();
        }

        private void injectDependencyService (ApplicationContext applicationContext){
            // 遍历容器中所有的 bean
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                Class<?> clazz = applicationContext.getType(beanName);
                if (clazz == null) {
                    continue;
                }

                // 遍历每个 bean 的成员属性，如果成员属性被 @PrpcRemoteReference 注解标记，说明依赖prpc远端接口
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    final PrpcRemoteReference annotation = field.getAnnotation(PrpcRemoteReference.class);
                    if (annotation == null) {
                        // 如果该成员属性没有标记该注解，继续找一下
                        continue;
                    }

                    // 终于找到被注解标记的成员属性了
                    Object beanObject = applicationContext.getBean(beanName);
                    Class<?> fieldClass = field.getType();
                    field.setAccessible(true); // 暴力反射
                    try {
                        // 给属性注入代理对象值
//                    field.set(beanObject, clientProxyFactory.getProxyInstance(fieldClass));
                    } catch (Exception e) {
                        log.error("Fail to inject service, bean.name: {}, error.msg: {}", beanName, e.getMessage());
                    }
                }
            }
        }

    }

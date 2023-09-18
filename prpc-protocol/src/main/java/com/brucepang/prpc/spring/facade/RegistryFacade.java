package com.brucepang.prpc.spring.facade;

import com.brucepang.prpc.util.HostAndPort;
import com.brucepang.prpc.exception.PrpcException;
import com.brucepang.prpc.common.IRegistryService;
import com.brucepang.prpc.eureka.EurekaRegistryService;
import com.brucepang.prpc.nacos.NacoasRegistryService;
import com.brucepang.prpc.zookeeper.ZookeeperRegistryService;
import com.brucepang.prpc.singleton.GenericSingletonFactory;
import com.brucepang.prpc.spring.properties.PrpcProperties;
import org.apache.commons.lang3.StringUtils;


import java.util.Map;

import static com.brucepang.prpc.config.RegistryDefaultConfig.*;


/**
 * 注册中心门面设计
 *
 * @author BrucePang
 */

public class RegistryFacade  {

    private static String host;
    private static int port;

    private static String parseAddress;


    /**
     * 通过配置文件动态生成注册中心实现类
     * @param prpcProperties 配置文件
     * @return
     */
    public static IRegistryService registrySelector(PrpcProperties prpcProperties) {
        if (prpcProperties == null) {
            throw new PrpcException(200000003, "properties can not be null!");
        }
        String address = prpcProperties.getAddress();
        if (address == null || "".equals(address.trim())) {
            // 本地调用
            return null;
        }

        return getRegistryService(prpcProperties.getAddress());

    }

    /**
     *
     * @param objectMap 被YamlToMap读取application.yml后转换的map对象
     * @return 注册中心的实现类
     */
    public static IRegistryService registrySelector(Map<String,Object> objectMap) {
        if (objectMap == null) {
            throw new PrpcException(200000003, "properties can not be null!");
        }
        String address = (String) objectMap.get("com.brucepang.prpc.address");
        if (address == null || "".equals(address.trim())) {
            // 本地调用
            return null;
        }

        return getRegistryService(address);

    }

    private static IRegistryService getRegistryService(String address) {
        getRegistryHostAndPort(address);
        parseAddress(address);
        try {
            if (address.contains(NACOS)) { // nacos://127.0.0.1:8848
                return GenericSingletonFactory.INSTANCE.getInstance(NacoasRegistryService.class,parseAddress);
            }

            if (address.contains(ZOOKEEPER)) { // zookeeper://127.0.0.1:2181 // TODO:还未测试BUG
                return GenericSingletonFactory.INSTANCE.getInstance(ZookeeperRegistryService.class,parseAddress);
            }

            if (address.contains(EUREKA)){ // TODO:还未测试BUG
                return GenericSingletonFactory.INSTANCE.getInstance(EurekaRegistryService.class,parseAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseAddress(String address) {
        if (StringUtils.isNoneEmpty(address)) {
            String prefix = "//";
            int prefixIndex = address.indexOf(prefix); // 第一处找到的位置
            if (prefixIndex == -1){
                throw new PrpcException(20000005,"please check your address!");
            }
            parseAddress = address.substring(prefixIndex + 2);
            return parseAddress;
        }
        return "";
    }



    public static HostAndPort getRegistryHostAndPort(String address) {
        if (StringUtils.isEmpty(address)){
            throw new PrpcException(200000006, "address can not be null!");
        }
        String prefix = "//";
        int prefixIndex = address.indexOf(prefix);
        if (prefixIndex == -1) {
            // 如果没有找到"//"前缀，可以根据需求返回一个默认值或者抛出异常
            throw new PrpcException(2000000001, "Address format is incorrect");
        }

        int colonIndex = address.lastIndexOf(":");
        if (colonIndex == -1) {
            // 如果没有找到":"，可以根据需求返回一个默认值或者抛出异常
            throw new PrpcException(2000000002, "Address format is incorrect");
        }

        // 提取host和port
        host = address.substring(prefixIndex + prefix.length(), colonIndex);
        port = Integer.parseInt(address.substring(colonIndex + 1));

        return new HostAndPort(host, port);

    }
}

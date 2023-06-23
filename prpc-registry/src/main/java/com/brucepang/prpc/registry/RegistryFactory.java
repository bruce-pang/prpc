package com.brucepang.prpc.registry;

import com.brucepang.prpc.registry.eureka.EurekaRegistryService;
import com.brucepang.prpc.registry.nacos.NacoasRegistryService;
import com.brucepang.prpc.registry.zookeeper.ZookeeperRegistryService;
import com.sun.xml.internal.bind.v2.TODO;

/**
 * @author BrucePang
 */
public class RegistryFactory {

        public static IRegistryService createRegistry(String address, RegistryType registryType){
            IRegistryService registryService = null;
            try {
                switch (registryType){
                    case ZOOKEEPER:
                        //TODO
                        registryService = new ZookeeperRegistryService(address);
                        break;
                    case EUREKA:
                        //TODO
                        registryService = new EurekaRegistryService(address);
                        break;
                    case NACOS:
                        registryService = new NacoasRegistryService(address);
                        break;
                    default:
                        registryService = new NacoasRegistryService(address);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return registryService;
        }

}

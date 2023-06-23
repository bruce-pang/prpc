package com.brucepang.prpc.registry.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.brucepang.prpc.registry.IRegistryService;
import com.brucepang.prpc.registry.ServiceInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author BrucePang
 */
@Slf4j
public class NacoasRegistryService implements IRegistryService {
    private static final String REGISTRY_PATH = "/registry";
    //nacos中提供的服务注册与发现的封装
    private ConfigService configService;//主要用作配置方面的管理功能
    private NamingService namingService;//主要用作服务方面的管理功能

    //初始化namingService和configService;
    public NacoasRegistryService(){

    }


    public NacoasRegistryService(String registryAddress) throws Exception {
        try {
            this.configService = NacosFactory.createConfigService(registryAddress);
            this.namingService = NacosFactory.createNamingService(registryAddress);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        log.info("begin registry servicinfo to Nacos server");
        namingService.registerInstance(serviceInfo.getServiceName(),serviceInfo.getServiceAddress(),serviceInfo.getServicePort());

    }

    // TODO 删除服务
    

    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        log.info("begin discover service from Nacos server");
        Instance instances = namingService.selectOneHealthyInstance(serviceName);
        if(instances!=null){
            ServiceInfo serviceInfo=new ServiceInfo();
            serviceInfo.setServiceName(serviceName);
            serviceInfo.setServiceAddress(instances.getIp());
            serviceInfo.setServicePort(instances.getPort());
            return serviceInfo;
        }
        return null;
    }


    //随机全部（有可能获取到的不健康）。拿到全部实例后，我们可以按照自己的负载均衡算法进行调用。类似于springcloud的ribbon。
    public List<Instance> getAllServer(String serverName) throws Exception{
        return namingService.getAllInstances(serverName);
    }

    //根据负载均衡算法获取一个健康的实例
    public Instance getOneHealthyInstance(String serverName) throws Exception{
        return namingService.selectOneHealthyInstance(serverName);
    }

}

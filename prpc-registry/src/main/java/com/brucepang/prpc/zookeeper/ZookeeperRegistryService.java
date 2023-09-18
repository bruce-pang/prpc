package com.brucepang.prpc.zookeeper;

import com.brucepang.prpc.common.IRegistryService;
import com.brucepang.prpc.common.ServiceInfo;
import com.brucepang.prpc.loadbalance.ILoadBalance;
import com.brucepang.prpc.loadbalance.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

/**
 * @author BrucePang
 */
@Slf4j
public class ZookeeperRegistryService implements IRegistryService {
    private static final String REGISTRY_PATH = "/registry";
    //curator中提供的服务注册与发现的封装
    private final ServiceDiscovery<ServiceInfo> serverDiscovery;

    private ILoadBalance<ServiceInstance<ServiceInfo>> loadBalance;
    public ZookeeperRegistryService(String registryAddress) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(registryAddress, new ExponentialBackoffRetry(1000, 3));
        client.start();
        JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
        this.serverDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(client)
                .serializer(serializer)
                .basePath(REGISTRY_PATH)
                .build();
        this.serverDiscovery.start();
        this.loadBalance = new RandomLoadBalance();
    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        log.info("begin registry servicinfo to Zookeeper server");
        ServiceInstance<ServiceInfo> serviceInstance=ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName()) // 服务名称
                .address(serviceInfo.getServiceAddress()) // 服务地址
                .port(serviceInfo.getServicePort()) // 服务端口
                .payload(serviceInfo) // 服务负载
                .build();
        this.serverDiscovery.registerService(serviceInstance);
    }

    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        log.info("begin discover service from Zookeeper server");
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = this.serverDiscovery.queryForInstances(serviceName);
        // 动态路由
        ServiceInstance<ServiceInfo> serviceInstance = this.loadBalance.select((List<ServiceInstance<ServiceInfo>>) serviceInstances);
        if (serviceInstance != null) {
            return serviceInstance.getPayload();
        }
        return null;
    }


}

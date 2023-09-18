package com.brucepang.prpc.spring.proxy;

import com.brucepang.prpc.constants.ReqType;
import com.brucepang.prpc.constants.RpcConstant;
import com.brucepang.prpc.constants.SerialType;
import com.brucepang.prpc.core.*;
import com.brucepang.prpc.exception.PrpcException;
import com.brucepang.prpc.protocol.NettyClient;
import com.brucepang.prpc.common.IRegistryService;
import com.brucepang.prpc.common.ServiceInfo;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 被 @PrpcRemoteReference 注解标记的接口变量，会自动注入一个代理对象；
 * 在调用方法时会自动执行代理对象的 invoke 方法
 *
 * @author BrucePang
 */

@Slf4j
public class ClientProxyFactory {

    private IRegistryService iRegistryService;

    private NettyClient nettyClient;

    public ClientProxyFactory(IRegistryService iRegistryService, NettyClient nettyClient){
        this.iRegistryService = iRegistryService;
        this.nettyClient = nettyClient;
    }

    /**
     * 获取代理对象，绑定 invoke 行为
     *
     * @param clazz 接口 class 对象
     * @param <T>   类型
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 第一步：通过服务发现机制选择一个服务提供者暴露的服务
                String serviceName = clazz.getName(); // 类名
                final ServiceInfo serviceInfo = iRegistryService.discovery(serviceName);
                log.info("Rpc server instance list: {}", serviceInfo);
                if (serviceInfo == null) {
                    throw new PrpcException(20000004, "No rpc servers found.");
                }

                // 第二步：构造 rpc 请求对象 + 编码
                RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
                long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
                Header header = new Header(RpcConstant.MAGIC, SerialType.JSON_SERIAL.code(), ReqType.REQUEST.getCode(), requestId, 0);
                reqProtocol.setHeader(header);
                RpcRequest request = new RpcRequest();
                request.setClassName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParams(args);
                reqProtocol.setContent(request);


                // 第四步：调用 rpc client 开始发送消息
                RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
                RequestHolder.REQUEST_MAP.put(requestId, future);
                nettyClient.sendRequest(reqProtocol, iRegistryService);
                return future.getPromise().get().getData();
            }
        });
    }


}
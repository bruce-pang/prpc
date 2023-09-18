package com.brucepang.prpc.spring.reference;

import com.brucepang.prpc.constants.ReqType;
import com.brucepang.prpc.constants.RpcConstant;
import com.brucepang.prpc.constants.SerialType;
import com.brucepang.prpc.core.*;
import com.brucepang.prpc.protocol.NettyClient;
import com.brucepang.prpc.common.IRegistryService;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author BrucePang
 */
@Slf4j
public class PrpcInvokerProxy implements InvocationHandler {
    private String host;
    private int port;

    private IRegistryService registryService;

    // 本地调用使用开关
    private boolean enableRegistry;

    public PrpcInvokerProxy(String host, int port, boolean enableRegistry) {
        this.host = host;
        this.port = port;
        this.enableRegistry = enableRegistry;
    }

    public PrpcInvokerProxy(String host, int port, IRegistryService registryService, boolean enableRegistry) {
        this.host = host;
        this.port = port;
        this.registryService = registryService;
        this.enableRegistry = enableRegistry;
    }





    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("begin invoke target server");
        RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
        Header header = new Header(RpcConstant.MAGIC, SerialType.JSON_SERIAL.code(), ReqType.REQUEST.getCode(), requestId,0);
        reqProtocol.setHeader(header);
        RpcRequest request=new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        reqProtocol.setContent(request);


        //TODO 短链接，用完就关闭了，需要改造成长链接
        // 注册中心走无参构造方法
        if (this.enableRegistry){
            NettyClient nettyClient=new NettyClient();
            RpcFuture<RpcResponse> future=new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
            RequestHolder.REQUEST_MAP.put(requestId,future);
            nettyClient.sendRequest(reqProtocol,registryService);
            return future.getPromise().get().getData();
        }

        // 本地调用走有参构造方法
        NettyClient nettyClient=new NettyClient(host,port);
        RpcFuture<RpcResponse> future=new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
        RequestHolder.REQUEST_MAP.put(requestId,future);
        nettyClient.sendRequest(reqProtocol);
        return future.getPromise().get().getData();
    }

}

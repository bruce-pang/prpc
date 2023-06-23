package com.brucepang.prpc.protocol;

import com.brucepang.prpc.core.RpcProtocol;
import com.brucepang.prpc.core.RpcRequest;
import com.brucepang.prpc.handler.RpcClientInitializer;
import com.brucepang.prpc.registry.IRegistryService;
import com.brucepang.prpc.registry.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BrucePang
 * Netty Client
 */

@Slf4j
public class NettyClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
    private String serviceAddress;
    private int servicePort;



    public NettyClient(String serviceAddress, int servicePort) {
        log.info("begin init Netty Client,{},{}",serviceAddress,servicePort);
        bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }
    public NettyClient() {

        bootstrap=new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());

    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {
        final ChannelFuture future=bootstrap.connect(serviceAddress, servicePort).sync();
        future.addListener(listener->{
            if(future.isSuccess()){
                log.info("connect rpc server {} success.",serviceAddress);
            }else{
                log.error("connect rpc server {} failed. ",serviceAddress);
                future.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        log.info("begin transfer data");
        future.channel().writeAndFlush(protocol);
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol, IRegistryService registryService) throws Exception {
        ServiceInfo serviceInfo=registryService.discovery(protocol.getContent().getClassName());
        final ChannelFuture future=bootstrap.connect(serviceInfo.getServiceAddress(), serviceInfo.getServicePort()).sync();
        future.addListener(listener->{
            if(future.isSuccess()){
                log.info("connect rpc server {} success.",serviceInfo.getServiceAddress());
            }else{
                log.error("connect rpc server {} failed. ",serviceInfo.getServiceAddress());
                future.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        log.info("begin transfer data");
        future.channel().writeAndFlush(protocol);
    }
}

package com.brucepang.prpc.protocol;

import com.brucepang.prpc.handler.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BrucePang
 * Netty的服务
 */
@Slf4j
public class NettyServer{
    private String serverAddress; // 服务的地址
    private int serverPort; // 服务的端口

    public NettyServer(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startNettyServer() {
        log.info("NettyServer start !");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group( bossGroup, workerGroup)
                .channel( NioServerSocketChannel.class )
                .childHandler(new RpcServerInitializer());
        try {
            ChannelFuture future = bootstrap.bind(serverAddress, serverPort).sync();
            log.info("NettyServer start success on ip:{} port:{}", serverAddress, serverPort);
            future.channel().closeFuture().sync(); // 确认客户端断开链接才关闭服务器
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("NettyServer stop !");
        }

    }

}

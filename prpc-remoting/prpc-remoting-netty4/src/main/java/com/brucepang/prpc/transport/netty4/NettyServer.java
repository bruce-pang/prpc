package com.brucepang.prpc.transport.netty4;

import com.brucepang.prpc.Request;
import com.brucepang.prpc.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.xml.ws.Response;
import java.io.IOException;

public class NettyServer implements Server {

    private final int port;
    private ChannelFuture channelFuture;

    public NettyServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                        @Override
                        protected void initChannel(NioServerSocketChannel ch) throws Exception {
                            // Add your handlers here
                        }
                    });

            channelFuture = bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            throw new IOException("Failed to start server", e);
        }
    }

    @Override
    public void stop() throws IOException {
        if (channelFuture != null) {
            channelFuture.channel().closeFuture();
        }
    }

    @Override
    public Request receive() throws IOException {
        // Implement your method to receive request here
        return null;
    }

    @Override
    public void send(Response response) throws IOException {
        if (channelFuture == null) {
            throw new IOException("Server is not started");
        }

        channelFuture.channel().writeAndFlush(response);
    }
}
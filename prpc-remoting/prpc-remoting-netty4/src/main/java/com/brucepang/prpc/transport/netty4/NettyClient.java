package com.brucepang.prpc.transport.netty4;

import com.brucepang.prpc.Request;
import com.brucepang.prpc.Response;
import com.brucepang.prpc.client.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

public class NettyClient implements Client {

    private final String host;
    private final int port;
    private ChannelFuture channelFuture;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect() throws IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // Add your handlers here
                        }
                    });

            channelFuture = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            throw new IOException("Failed to connect to server", e);
        }
    }

    @Override
    public void send(Request request) throws IOException {
        if (channelFuture == null) {
            throw new IOException("Client is not connected");
        }

        channelFuture.channel().writeAndFlush(request);
    }

    @Override
    public Response receive() throws IOException {
        // Implement your method to receive response here
        return null;
    }

    @Override
    public void close() throws IOException {
        if (channelFuture != null) {
            channelFuture.channel().closeFuture();
        }
    }
}
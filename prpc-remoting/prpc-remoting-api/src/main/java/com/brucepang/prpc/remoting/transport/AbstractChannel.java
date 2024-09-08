package com.brucepang.prpc.remoting.transport;

import com.brucepang.prpc.remoting.Channel;
import com.brucepang.prpc.remoting.Request;
import com.brucepang.prpc.remoting.Response;

import java.net.InetSocketAddress;

public abstract class AbstractChannel implements Channel {

    protected InetSocketAddress localAddress;
    protected InetSocketAddress remoteAddress;

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public Response request(Request request) {
        if (!isConnected()) {
            throw new IllegalStateException("Channel is not connected");
        }
        send(request);
        // Here you should wait for the response and return it
        // The specific implementation depends on your network protocol
        return null;
    }

    @Override
    public void close() {
        if (isConnected()) {
            // Here you should close the connection
            // The specific implementation depends on your network protocol
        }
    }
}
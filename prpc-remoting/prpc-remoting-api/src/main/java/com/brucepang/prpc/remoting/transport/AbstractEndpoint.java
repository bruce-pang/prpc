package com.brucepang.prpc.remoting.transport;

import com.brucepang.prpc.common.URL;
import com.brucepang.prpc.remoting.Endpoint;
import com.brucepang.prpc.remoting.RemotingException;

import java.net.InetSocketAddress;

/**
 * @author BrucePang
 */
public abstract class AbstractEndpoint implements Endpoint {
    @Override
    public void close() {

    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public Class<?> getInterface() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public void send(Object message) throws RemotingException {

    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public void startClose() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }
}

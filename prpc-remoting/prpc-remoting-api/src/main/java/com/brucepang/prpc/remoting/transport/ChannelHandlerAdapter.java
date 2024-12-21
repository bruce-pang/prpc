package com.brucepang.prpc.remoting.transport;

import com.brucepang.prpc.remoting.Channel;
import com.brucepang.prpc.remoting.ChannelHandler;
import com.brucepang.prpc.remoting.RemotingException;

/**
 * @author BrucePang
 */
public class ChannelHandlerAdapter implements ChannelHandler {
    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {

    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {

    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {

    }
}

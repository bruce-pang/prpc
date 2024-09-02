package com.brucepang.prpc.rpc;

import com.brucepang.prpc.api.Channel;

import java.net.InetSocketAddress;

/**
 * @author BrucePang
 */
public class RemotingException extends Exception {

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemotingException(Channel channel, String msg) {
        this(
                channel == null ? null : channel.getLocalAddress(),
                channel == null ? null : channel.getRemoteAddress(),
                msg);
    }


}

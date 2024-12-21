package com.brucepang.prpc.rpc;

import com.brucepang.prpc.remoting.api.Channel;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;

/**
 * @author BrucePang
 */
@SPI(scope = ExtensionScope.GLOBAL)
public interface ChannelHandler {
    /**
     * on channel connected.
     *
     * @param channel channel.
     */
    void connected(Channel channel) throws Exception;

    /**
     * on channel disconnected.
     *
     * @param channel channel.
     */
    void disconnected(Channel channel) throws Exception;

    /**
     * on message sent.
     *
     * @param channel channel.
     * @param message message.
     */
    void sent(Channel channel, Object message) throws RemotingException;

    
}

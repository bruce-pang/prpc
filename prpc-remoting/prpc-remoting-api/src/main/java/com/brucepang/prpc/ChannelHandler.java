package com.brucepang.prpc;

import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;

/**
 * Channel handler interface

 */
@SPI(scope = ExtensionScope.GLOBAL)
public interface ChannelHandler {

    /**
     * on channel connected.
     *
     * @param channel channel.
     */
    void connected(Channel channel);

    /**
     * on channel disconnected.
     *
     * @param channel channel.
     */
    void disconnected(Channel channel);

    /**
     * on message sent.
     *
     * @param channel channel.
     * @param message message.
     */
    void sent(Channel channel, Object message);
}
package com.brucepang.prpc;

/**
 * Channel handler interface

 */
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

}
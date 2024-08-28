package com.brucepang.prpc.rpc;

import com.brucepang.prpc.api.Channel;
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
}

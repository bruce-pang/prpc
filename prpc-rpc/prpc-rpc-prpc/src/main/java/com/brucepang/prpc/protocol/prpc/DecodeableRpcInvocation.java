package com.brucepang.prpc.protocol.prpc;

import com.brucepang.prpc.Channel;
import com.brucepang.prpc.buffer.ChannelBuffer;
import com.brucepang.prpc.rpc.Codec;
import com.brucepang.prpc.rpc.protocol.RpcInvocation;

import java.io.IOException;

/**
 *
 * @author BrucePang
 */
public class DecodeAbleRpcInvocation extends RpcInvocation implements Codec {



    @Override
    public void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException {

    }

    @Override
    public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
        return null;
    }
}

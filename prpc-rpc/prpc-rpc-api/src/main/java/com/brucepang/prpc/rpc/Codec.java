package com.brucepang.prpc.rpc;

import com.brucepang.prpc.remoting.Channel;
import com.brucepang.prpc.remoting.buffer.ChannelBuffer;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;
import com.brucepang.prpc.extension.inject.Adaptive;

import java.io.IOException;

@SPI(scope = ExtensionScope.GLOBAL)
public interface Codec {

    @Adaptive({Constants.CODEC_KEY})
    void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException;

    @Adaptive({Constants.CODEC_KEY})
    Object decode(Channel channel, ChannelBuffer buffer) throws IOException;

    enum DecodeResult {
        NEED_MORE_INPUT,
        SKIP_SOME_INPUT
    }
}

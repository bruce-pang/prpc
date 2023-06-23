package com.brucepang.prpc.codec;


import com.brucepang.prpc.core.Header;
import com.brucepang.prpc.core.RpcProtocol;
import com.brucepang.prpc.serialize.ISerializer;
import com.brucepang.prpc.serialize.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BrucePang
 * Rpc编码器
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        log.info("============begin RpcEncoder=========");
        Header header=msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getSerialType());
        out.writeByte(header.getReqType());
        out.writeLong(header.getRequestId());
        ISerializer serializer= SerializerManager.getSerializer(header.getSerialType());
        byte[] data=serializer.serialize(msg.getContent());
        out.writeInt(data.length); // 消息长度
        out.writeBytes(data);
    }
}

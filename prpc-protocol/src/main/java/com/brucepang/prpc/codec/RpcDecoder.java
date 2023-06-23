package com.brucepang.prpc.codec;

import com.brucepang.prpc.constants.ReqType;
import com.brucepang.prpc.constants.RpcConstant;
import com.brucepang.prpc.core.Header;
import com.brucepang.prpc.core.RpcProtocol;
import com.brucepang.prpc.core.RpcRequest;
import com.brucepang.prpc.core.RpcResponse;
import com.brucepang.prpc.serialize.ISerializer;
import com.brucepang.prpc.serialize.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author BrucePang
 * Rpc解码器
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 解码
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("========begin RpcDecoder==========");

        if(in.readableBytes()< RpcConstant.HEAD_TOTOAL_LEN){
            return;
        }
        in.markReaderIndex(); //标记读取开始索引
        short maci=in.readShort(); //读取2个字节的magic
        if(maci!=RpcConstant.MAGIC){
            throw new IllegalArgumentException("Illegal request parameter 'magic',"+maci);
        }

        byte serialType=in.readByte(); //读取一个字节的序列化类型
        byte reqType=in.readByte(); //读取一个字节的消息类型
        long requestId=in.readLong(); //读取请求id
        int dataLength=in.readInt(); //读取数据报文长度

        if(in.readableBytes()<dataLength){
            in.resetReaderIndex();
            return ;
        }
        //读取消息体的内容
        byte[] content=new byte[dataLength];
        in.readBytes(content);

        Header header=new Header(maci,serialType,reqType,requestId,dataLength);
        ISerializer serializer= SerializerManager.getSerializer(serialType); // 根据类型获取序列化的类型
        ReqType rt=ReqType.findByCode(reqType);
        switch (rt){
            case REQUEST:
                RpcRequest request=serializer.deserialize(content,RpcRequest.class);
                RpcProtocol<RpcRequest> reqProtocol=new RpcProtocol<>();
                reqProtocol.setHeader(header);
                reqProtocol.setContent(request);
                out.add(reqProtocol);
                break;
            case RESPONSE:
                RpcResponse response=serializer.deserialize(content, RpcResponse.class);
                RpcProtocol<RpcResponse> resProtocol=new RpcProtocol<>();
                resProtocol.setHeader(header);
                resProtocol.setContent(response);
                out.add(resProtocol);
                break;
            case HEARTBEAT:
                //TODO
                break;
            default:
                break;
        }

    }
}

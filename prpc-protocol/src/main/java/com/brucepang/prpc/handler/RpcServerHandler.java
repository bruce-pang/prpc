package com.brucepang.prpc.handler;

import com.brucepang.prpc.constants.ReqType;
import com.brucepang.prpc.core.Header;
import com.brucepang.prpc.core.RpcProtocol;
import com.brucepang.prpc.core.RpcRequest;
import com.brucepang.prpc.core.RpcResponse;
import com.brucepang.prpc.handler.reflect.Mediator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author BrucePang
 * 服务端的处理器
 */
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        // 反射调用
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol();
        // 构建响应的消息
        Header header = msg.getHeader(); // 请求头
        header.setReqType(ReqType.RESPONSE.getCode());
//        Object result = invoke(msg.getContent());
        Object result = Mediator.getInstance().processor(msg.getContent()); // 在这里反射调用了具体实现类的方法
        resProtocol.setHeader(header);
        RpcResponse response = new RpcResponse();
        response.setData(result);
        response.setMsg("success");
        resProtocol.setContent(response);
        ctx.writeAndFlush(resProtocol);
    }

}

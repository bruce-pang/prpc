package com.brucepang.prpc.handler;

import com.brucepang.prpc.core.RequestHolder;
import com.brucepang.prpc.core.RpcFuture;
import com.brucepang.prpc.core.RpcProtocol;
import com.brucepang.prpc.core.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BrucePang
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        log.info("receive Rpc Server Result");
        long requestId=msg.getHeader().getRequestId();
        RpcFuture<RpcResponse> future= RequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getContent()) ; //返回结果
    }
}

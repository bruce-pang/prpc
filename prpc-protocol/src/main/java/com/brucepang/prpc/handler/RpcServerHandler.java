package com.brucepang.prpc.handler;

import com.brucepang.prpc.constants.ReqType;
import com.brucepang.prpc.core.Header;
import com.brucepang.prpc.core.RpcProtocol;
import com.brucepang.prpc.core.RpcRequest;
import com.brucepang.prpc.core.RpcResponse;
import com.brucepang.prpc.spring.SpringBeanManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author BrucePang
 * 服务端的处理器
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        // 反射调用
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol();
        // 构建响应的消息
        Header header = msg.getHeader(); // 请求头
        header.setReqType(ReqType.RESPONSE.getCode());
        Object result = invoke(msg.getContent());
        resProtocol.setHeader(header);
        RpcResponse response = new RpcResponse();
        response.setData(result);
        response.setMsg("success");
        resProtocol.setContent(response);
        ctx.writeAndFlush(resProtocol);
    }

    /**
     * 反射调用
     * @param request
     * @return
     */
    private Object invoke(RpcRequest request) {
        try {
            Class<?> clazz = Class.forName(request.getClassName());
            Object bean = SpringBeanManager.getBean(clazz); // 获取目标类的实例
            Method method = clazz.getDeclaredMethod(request.getMethodName(), request.getParameterTypes()); // 获取目标方法
            // 允许暴力反射
            method.setAccessible(true);
            return method.invoke(bean, request.getParams()); // 反射调用目标方法
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

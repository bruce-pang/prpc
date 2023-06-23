package com.brucepang.prpc.core;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author BrucePang
 * 请求体，由于rpc远程调用，所以需要反射
 */
@Data
public class RpcRequest implements Serializable {
    private String className; //类名
    private String methodName; //请求目标方法
    private Object[] params;  //请求参数
    private Class<?>[] parameterTypes; //参数类型
}

package com.brucepang.prpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author BrucePang
 * 响应
 */
@Data
public class RpcResponse implements Serializable {
    private Object data; //响应数据
    private String msg; // 响应消息
}

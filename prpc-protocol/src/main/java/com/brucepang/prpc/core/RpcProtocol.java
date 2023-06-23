package com.brucepang.prpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author BrucePang
 * 数据包
 */
@Data
public class RpcProtocol<T> implements Serializable {
    private Header header;
    private T  content;

}

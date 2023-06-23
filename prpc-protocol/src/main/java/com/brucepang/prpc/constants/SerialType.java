package com.brucepang.prpc.constants;

import lombok.Data;

/**
 * @author BrucePang
 * 序列化类型
 */
public enum SerialType {
    JSON_SERIAL((byte)1),
    JAVA_SERIAL((byte)2);

    private byte code;

    SerialType(byte code){
        this.code=code;
    }

    public byte code(){
        return this.code;
    }
}

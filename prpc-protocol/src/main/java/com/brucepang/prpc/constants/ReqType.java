package com.brucepang.prpc.constants;

/**
 * @author BrucePang
 * 请求类型
 */
public enum ReqType {
    REQUEST((byte)1),
    RESPONSE((byte)2),
    HEARTBEAT((byte)3);
    private byte code;

    ReqType(byte code) {
        this.code = code;
    }

    public byte code(){
        return this.code;
    }
    public static ReqType findByCode(int reqType) {
        for (ReqType value : ReqType.values()) {
            if(value.getCode()==reqType){
                return value;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }
}

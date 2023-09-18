package com.brucepang.prpc.common;

/**
 * @author BrucePang
 *
 **/
public enum RegistryType {

    NACOS((byte)0),
    EUREKA((byte)1),
    ZOOKEEPER ((byte)2);
    private byte code;

    RegistryType(byte code){
        this.code=code;
    }

    public static RegistryType findByCode(int code){
        for(RegistryType reqType: RegistryType.values()){
            if(reqType.code==code){
                return reqType;
            }
        }
        return null;
    }

    public byte code(){
        return this.code;
    }
}

package com.brucepang.prpc.registry;

/**
 * @author BrucePang
 *
 **/
public enum RegistryType {

    ZOOKEEPER((byte)0),
    EUREKA((byte)1),
    NACOS ((byte)2);
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

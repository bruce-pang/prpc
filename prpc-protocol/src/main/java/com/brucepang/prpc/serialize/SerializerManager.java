package com.brucepang.prpc.serialize;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BrucePang
 * 序列化管理器
 */
public class SerializerManager {

    private final static ConcurrentHashMap<Byte,ISerializer> serializer=new ConcurrentHashMap<>();

    static{
        ISerializer json=new JsonSerializer();
        ISerializer java=new JavaSerializer();
        serializer.put(json.getType(),json);
        serializer.put(java.getType(),java);
    }
    public static ISerializer getSerializer(byte key){
        ISerializer iSerializer=serializer.get(key);
        if(serializer==null){
            return new JavaSerializer();
        }
        return iSerializer;
    }
}

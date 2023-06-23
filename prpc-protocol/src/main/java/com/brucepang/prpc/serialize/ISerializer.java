package com.brucepang.prpc.serialize;

/**
 * @author BrucePang
 * 序列化接口
 */
public interface ISerializer {
    /*
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data,Class<T> clazz);

    /**
     * 序列化的类型
     * @return
     */
    byte getType();
}

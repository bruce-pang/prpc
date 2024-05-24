package com.brucepang.prpc.common.serialize;

import com.brucepang.prpc.common.serialize.support.Serialization;

import java.io.*;

/**
 * Java serialization
 */
public class JavaSerialization implements Serialization {

    /**
     * Serialize object to byte array
     * @param obj object
     * @param <T> object type
     * @return byte array
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        return bos.toByteArray();
    }

    /**
     * Deserialize byte array to object
     * @param data byte array
     * @param cls object class
     * @return object
     * @param <T> object type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return cls.cast(obj);
    }
}

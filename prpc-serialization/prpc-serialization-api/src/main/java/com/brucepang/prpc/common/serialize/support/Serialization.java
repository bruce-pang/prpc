package com.brucepang.prpc.common.serialize.support;

import java.io.IOException;

/**
 * The Serialization interface.
 */
public interface Serialization {

    /**
     * Serialize the given object to byte array.
     *
     * @param obj the object to serialize
     * @return the byte array representation of the object
     * @throws IOException if any IO errors occur
     */
    byte[] serialize(Object obj) throws IOException;

    /**
     * Deserialize the given byte array to an object.
     *
     * @param data the byte array to deserialize
     * @param clazz the class of the object
     * @return the deserialized object
     * @throws IOException if any IO errors occur
     * @throws ClassNotFoundException if the class of the object is not found
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException, ClassNotFoundException;
}
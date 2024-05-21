package com.brucepang.prpc.common.serialize;

import java.io.IOException;

public interface Serialization {
    <T> byte[] serialize(T obj) throws IOException;
    <T> T deserialize(byte[] data, Class<T> cls) throws IOException, ClassNotFoundException;
}
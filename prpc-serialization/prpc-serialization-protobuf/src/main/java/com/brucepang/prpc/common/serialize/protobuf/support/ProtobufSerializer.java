package com.brucepang.prpc.common.serialize.protobuf.support;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

/**
 * A simple Serializer For Protobuf
 *
 * @author BrucePang
 */
public class ProtobufSerializer {

    public <T extends Message> byte[] serialize(T message) {
        return message.toByteArray();
    }

    public <T extends Message> T deserialize(byte[] data, Message.Builder builder) throws InvalidProtocolBufferException {
        builder.mergeFrom(data);
        return (T) builder.build();
    }
}
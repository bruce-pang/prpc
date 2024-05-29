package com.brucepang.prpc.exchange.codec;

import com.brucepang.prpc.Codec;
import com.brucepang.prpc.Request;
import com.brucepang.prpc.Response;

import java.io.*;
/**
 * Default exchange codec
 */
public class DefaultExchangeCodec implements Codec {

    @Override
    public byte[] encode(Request request) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(request);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode request", e);
        }
    }

    @Override
    public Response decode(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to decode response", e);
        }
    }
}
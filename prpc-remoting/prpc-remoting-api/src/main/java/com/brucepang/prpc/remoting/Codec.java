package com.brucepang.prpc.remoting;
/**
 * codec interface
 */
public interface Codec {

    byte[] encode(Request request);

    Response decode(byte[] bytes);
}
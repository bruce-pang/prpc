package com.brucepang.prpc;
/**
 * codec interface
 */
public interface Codec {

    byte[] encode(Request request);

    Response decode(byte[] bytes);
}
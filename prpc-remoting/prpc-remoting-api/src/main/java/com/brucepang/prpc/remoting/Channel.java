package com.brucepang.prpc.remoting;

import java.net.InetSocketAddress;

/**
 * Channel interface
 */
public interface Channel extends Endpoint {

    /**
     * Get the local address of the channel.
     *
     * @return the local address
     */
    InetSocketAddress getLocalAddress();

    /**
     * Get the remote address of the channel.
     *
     * @return the remote address
     */
    InetSocketAddress getRemoteAddress();

    /**
     * Send a request and get the response.
     *
     * @param request the request
     * @return the response
     */
    Response request(Request request);

    /**
     * Check if the channel is connected.
     *
     * @return true if the channel is connected, false otherwise
     */
    boolean isConnected();

    /**
     * Send a request.
     *
     * @param request the request
     */
    void send(Request request);

    /**
     * Close the channel.
     */
    void close();
}
package com.brucepang.prpc.api;

import com.brucepang.prpc.Request;
import com.brucepang.prpc.Response;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface Channel {

    /**
     * Send a request to the remote service.
     *
     * @param request the request to send
     * @throws IOException if any IO errors occur
     */
    void send(Request request) throws IOException;

    /**
     * Receive a response from the remote service.
     *
     * @return the response from the remote service
     * @throws IOException if any IO errors occur
     */
    Response receive() throws IOException;

    /**
     * Check if the channel is connected.
     *
     * @return true if the channel is connected, false otherwise
     */
    boolean isConnected();

    /**
     * get remote address.
     *
     * @return remote address.
     */
    InetSocketAddress getRemoteAddress();

}
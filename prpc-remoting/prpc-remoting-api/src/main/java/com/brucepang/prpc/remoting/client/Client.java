package com.brucepang.prpc.remoting.client;

import com.brucepang.prpc.remoting.Request;

import com.brucepang.prpc.remoting.Response;
import java.io.IOException;

public interface Client {

    /**
     * Connect to the remote service.
     *
     * @throws IOException if any IO errors occur
     */
    void connect() throws IOException;

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
     * Close the client.
     *
     * @throws IOException if any IO errors occur
     */
    void close() throws IOException;
}
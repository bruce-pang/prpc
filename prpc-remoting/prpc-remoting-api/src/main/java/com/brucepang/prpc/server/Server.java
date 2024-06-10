package com.brucepang.prpc.server;

import com.brucepang.prpc.Request;
import com.brucepang.prpc.Response;

import java.io.IOException;

public interface Server {

    /**
     * Start the server.
     *
     * @throws IOException if any IO errors occur
     */
    void start() throws IOException;

    /**
     * Stop the server.
     *
     * @throws IOException if any IO errors occur
     */
    void stop() throws IOException;

    /**
     * Receive a request from the client.
     *
     * @return the request from the client
     * @throws IOException if any IO errors occur
     */
    Request receive() throws IOException;

    /**
     * Send a response to the client.
     *
     * @param response the response to send
     * @throws IOException if any IO errors occur
     */
    void send(Response response) throws IOException;
}
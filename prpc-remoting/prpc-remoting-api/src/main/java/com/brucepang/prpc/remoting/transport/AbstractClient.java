package com.brucepang.prpc.remoting.transport;

import com.brucepang.prpc.remoting.Request;
import com.brucepang.prpc.remoting.Response;
import com.brucepang.prpc.remoting.client.Client;

import java.io.IOException;

/**
 * @author BrucePang
 */
public abstract class AbstractClient implements Client {
    @Override
    public void connect() throws IOException {

    }

    @Override
    public void send(Request request) throws IOException {

    }

    @Override
    public Response receive() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}

package com.brucepang.prpc.rpc;

import com.brucepang.prpc.URL;
import com.brucepang.prpc.extension.SPI;

/**
 * @author BrucePang
 * @description Protocol.
 */
@SPI("prpc")
public interface Protocol {

    /**
     * Export a service for remote invocation.
     *
     * @param service the service to be exported
     * @return the exported service
     */
    <T> Exporter<T> export(Invoker<T> service);

    /**
     * Refer a remote service.
     *
     * @param type the interface of the service
     * @param url the url of the remote service
     * @return a proxy of the remote service
     */
    <T> Invoker<T> refer(Class<T> type, URL url);

    /**
     * Destroy or close the protocol.
     */
    void destroy();
}

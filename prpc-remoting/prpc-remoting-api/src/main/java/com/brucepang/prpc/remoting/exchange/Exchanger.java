package com.brucepang.prpc.remoting.exchange;

import com.brucepang.prpc.remoting.Request;
import com.brucepang.prpc.remoting.Response;
/**
 * Exchanger interface
 */
public interface Exchanger {

    Response exchange(Request request);
}
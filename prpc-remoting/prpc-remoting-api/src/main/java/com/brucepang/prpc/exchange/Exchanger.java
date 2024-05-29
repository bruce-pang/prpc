package com.brucepang.prpc.exchange;

import com.brucepang.prpc.Request;
import com.brucepang.prpc.Response;
/**
 * Exchanger interface
 */
public interface Exchanger {

    Response exchange(Request request);
}
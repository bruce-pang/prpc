package com.brucepang.prpc.loadbalance;

import java.util.List;

/**
 * @author BrucePang
 */
public interface ILoadBalance<T> {

    T select(List<T> servers);

}

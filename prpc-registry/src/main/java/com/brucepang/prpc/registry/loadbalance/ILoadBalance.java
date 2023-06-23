package com.brucepang.prpc.registry.loadbalance;

import java.util.List;

/**
 * @author BrucePang
 */
public interface ILoadBalance<T> {

    T select(List<T> servers);

}

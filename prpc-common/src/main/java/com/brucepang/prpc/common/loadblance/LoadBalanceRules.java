package com.brucepang.prpc.common.loadblance;

/**
 * constant for Load Balance Strategy
 *
 * @author BrucePang
 */
public interface LoadBalanceRules {

    /**
     *  This class select one provider from multiple providers randomly.
     **/
    String RANDOM = "random";

    /**
     *  Round robin load balance.
     **/
    String ROUND_ROBIN = "round_robin";
}

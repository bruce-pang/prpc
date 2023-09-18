package com.brucepang.prpc.provider.example.service;

import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.api.example.DogBarkingService;

/**
 * @author BrucePang
 */
@PrpcRemoteService
public class DogBarkingServiceImpl implements DogBarkingService {
    @Override
    public String dontDogBarking(String name) {
        return name + ", 你在 U•ェ•*U 叫什么？？？";
    }
}

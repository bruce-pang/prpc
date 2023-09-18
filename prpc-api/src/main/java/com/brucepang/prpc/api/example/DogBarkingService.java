package com.brucepang.prpc.api.example;

/**
 * @author BrucePang
 */
public interface DogBarkingService {
    /**
     * 见名知意
     * @param name 名字
     * @return 一段寓意较深的话
     */
    String dontDogBarking(String name);
}

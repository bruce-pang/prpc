package com.brucepang.prpc.api.example;

/**
 * @author BrucePang
 */
public interface PersonService {
    /**
     *
     * @param name 保存用户信息
     * @return 向name打招呼
     */
    String save(String name);
}

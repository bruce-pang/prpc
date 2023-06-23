package com.brucepang.prpc.provider.example.service;

import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.api.example.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author BrucePang
 */
@PrpcRemoteService // 标记为远程服务对象
@Slf4j
public class PersonServiceImpl implements IPersonService {
    @Override
    public String save(String name) {
        log.info("Hello,{},您好！", name);
        return "Hello," + name + "您好!";
    }
}

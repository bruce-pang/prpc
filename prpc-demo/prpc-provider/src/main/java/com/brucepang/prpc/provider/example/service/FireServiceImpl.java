package com.brucepang.prpc.provider.example.service;

import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.api.example.FireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author BrucePang
 */
@PrpcRemoteService // 标记为远程服务对象
@Slf4j
public class FireServiceImpl implements FireService {
    @Override
    public String fire(String name) {
        log.info("向{}开炮！", name);
        return "向" + name + "开炮！";
    }
}

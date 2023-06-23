package com.brucepang.prpc.provider.example.service;

import com.brucepang.prpc.api.example.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author BrucePang
 */
@Service
@Slf4j
public class PersonServiceImpl implements IPersonService {
    @Override
    public String save(String name) {
        log.info("save person name:{}", name);
        return name;
    }
}

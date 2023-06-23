package com.brucepang.prpc.consumer.example.controller;

import com.brucepang.prpc.annotation.PrpcRemoteReference;
import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.api.example.IFireService;
import com.brucepang.prpc.api.example.IPersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BrucePang
 */
@RestController
public class HelloController {
    @PrpcRemoteReference
    IPersonService personService;

    @PrpcRemoteReference
    IFireService fireService;

    @GetMapping("/save")
    public String save(String name) {
        return personService.save(name);
    }

    @GetMapping("/fire")
    public String fire(String name) {
        return fireService.fire(name);
    }
}

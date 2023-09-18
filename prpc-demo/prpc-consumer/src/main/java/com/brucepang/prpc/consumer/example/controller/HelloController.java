package com.brucepang.prpc.consumer.example.controller;

import com.brucepang.prpc.annotation.PrpcRemoteReference;
import com.brucepang.prpc.annotation.PrpcRemoteService;
import com.brucepang.prpc.api.example.DogBarkingService;
import com.brucepang.prpc.api.example.FireService;
import com.brucepang.prpc.api.example.PersonService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BrucePang
 */
@RestController
@Slf4j
public class HelloController {
    @PrpcRemoteReference
    private PersonService personService;

    @PrpcRemoteReference
    private FireService fireService;

    @PrpcRemoteReference
    private DogBarkingService dogBarkingService;

    @GetMapping("/hello")
    public String hello(String name) {
        String s = personService.save(name);
        log.info("success from prpc. msg: {}", s);
        return s;
    }

    @GetMapping("/fire")
    public String fire(String name) {
        return fireService.fire(name);
    }

    @GetMapping("/dogbark/{name}")
    public String dogBack(@PathVariable String name ){
        return dogBarkingService.dontDogBarking(name);
    }
}

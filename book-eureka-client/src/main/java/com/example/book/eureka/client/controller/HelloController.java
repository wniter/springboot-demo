package com.example.book.eureka.client.controller;



import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.cloud.client.ServiceInstance;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @create 2022-03-08 12:33
 */

@RestController
public class HelloController {
    private final Logger logger = Logger.getLogger(String.valueOf(getClass()));


    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
        ServiceInstance instance = (ServiceInstance) client.getServices();
        logger.info("/hello, host:" + instance.getHost() + "， service id:" +
        instance.getServiceId());

        return "Hello World";
    }
}
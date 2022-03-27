package com.example.book.eureka.server.controller;


import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * @create 2022-03-07 23:28
 */
//@RestController
//public class HelloController {
//    private final Logger logger = Logger.getLogger(String.valueOf(HelloController.class));
//
//    @Autowired
//    private DiscoveryClient client;
//
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
//    public String index() {
//        ServiceInstance instance = client.getServiceId();
//        logger.info("/hello, host:" + instance.getHost() + "， service id:" +
//                instance.getServiceId());
//        return "Hello World";
//    }
//
//}

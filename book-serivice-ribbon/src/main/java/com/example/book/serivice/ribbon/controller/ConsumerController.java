package com.example.book.serivice.ribbon.controller;

import com.example.book.serivice.ribbon.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @create 2022-03-08 14:10
 */

@RestController
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HelloService helloService;
    @RequestMapping(value = "/ribbonconsumer", method = RequestMethod.GET)
    public String helloComsumer() {
//        return restTemplate.getForEntity("http://HELLO_SERVICE//hello",String.class).getBody();
        return helloService.helloService();
    }
}

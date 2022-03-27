package com.example.book.serivice.ribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @create 2022-03-08 16:43
 */
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    //fallbackMethod指定处理回退逻辑的方法。
    @HystrixCommand(fallbackMethod = "helloFallback")
    public String helloService() {
        return restTemplate.getForEntity("http://HELLO_SERVICE//hello",String.class).getBody();
    }

    public String helloFallback() {
        return "error";
    }

}

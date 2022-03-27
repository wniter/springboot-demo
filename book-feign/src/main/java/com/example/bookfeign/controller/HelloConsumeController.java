package com.example.bookfeign.controller;

import com.example.bookfeign.service.HelloService;
import com.example.bookfeign.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

/**
 * * @RequestBody和@RequestParam注解的区别和作用 区别：
 * * <p>
 * * 1、在请求上的区别：@RequestBody用于Post请求    @RequestParam用于Get请求
 * <p>
 * * @RequestBody用于将前端传递的JSON参数转化为一个整体对象
 * * @RequestParam用于接收url中的key-value参数的传递
 * *
 *
 * @create 2022-03-08 21:30
 */
@RestController
public class HelloConsumeController {


    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/fegin-consumer", method = RequestMethod.GET)
    public String helloConsumer() {
        return helloService.hello();
    }

    @RequestMapping(value = "/feign-consumer2", method = RequestMethod.GET)
    public String helloConsumer2() {
        StringBuilder sb = new StringBuilder();
        sb.append(helloService.hello()).append("\n");
        sb.append(helloService.hello("DIDI")).append("\n");
        sb.append(helloService.hello("DIDI", 30)).append("\n");
        sb.append(helloService.hello(new User("DIDI", 30))).append("\n");
        return sb.toString();
    }
}




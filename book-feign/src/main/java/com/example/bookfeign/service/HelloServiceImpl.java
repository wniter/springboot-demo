package com.example.bookfeign.service;

import com.example.bookfeign.user.User;
import org.springframework.web.bind.annotation.*;

/**
 * @create 2022-03-08 23:58
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello() {
        return "hello world ";
    }

    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    public String hello(@RequestParam("name") String name) {
        return "hello" + name;
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public User hello(@RequestHeader String name, Integer age) {
        return new User(name, age);
    }
    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    public String hello(@RequestBody User user) {
        return  user.toString();
    }
}

package com.example.book.restful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @create 2022-03-07 21:34
 */
@RestController
//@RequestMapping("")
public class HelloworldController {

    @RequestMapping(value="/helloworld",method = RequestMethod.GET)
    String HelloRestController(){
        return "hello world";
    }
}

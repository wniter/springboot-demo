package com.example.bookfeign.service;

import com.example.bookfeign.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import sun.awt.SunHints;

/**
 * @create 2022-03-08 21:25
 */
//通过@FeignClient注解指定服务名来绑定服务，然后使用SpringMVC的注解绑定具体服务提供的REST接口
@FeignClient("hello-service")
public interface HelloService {

    @RequestMapping("/hello")
    String hello();

//在定义各参数绑定时，@RequestParam、@RequestHeader 等可
//以指定参数名称的注解， 它们的 value 千万不能少。 在SpringMVC 程序中， 这些注解会根
//据参数名来作为默认值，但是在Feign 中绑定参数必须通过 value 属性来指明具体的参数名，
//不然会抛出口legalStateException 异常， value 属性不能为空
    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    String hello(@RequestParam("name") String name);

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public User hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    public String hello(@RequestBody User user);

}

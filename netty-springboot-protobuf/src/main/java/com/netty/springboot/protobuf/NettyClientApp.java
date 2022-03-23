package com.netty.springboot.protobuf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @create 2022-01-15 23:54
 */
@SpringBootApplication
@ComponentScan({"com.netty.springboot.protobuf.client"})
public class NettyClientApp {
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(NettyClientApp.class);
    }
}

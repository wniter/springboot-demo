package com.netty.springboot.protobuf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @create 2022-01-15 23:55
 */
@SpringBootApplication
@ComponentScan({"com.netty.springboot.protobuf.server"})
public class NettyServerApp {
    /**
     * 快捷键：/**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(NettyServerApp.class);
    }
}

package com.example.nettywebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.nettywebflux"})
public class NettyWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyWebfluxApplication.class, args);
    }

}

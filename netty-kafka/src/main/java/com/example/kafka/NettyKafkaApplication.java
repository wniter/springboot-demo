package com.example.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyKafkaApplication {
    public static ConfigurableApplicationContext context;
    public static void main(String[] args) {
        SpringApplication.run(NettyKafkaApplication.class, args);
    }

}

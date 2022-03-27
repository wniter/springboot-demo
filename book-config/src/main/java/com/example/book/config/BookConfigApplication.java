package com.example.book.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class BookConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookConfigApplication.class, args);
    }

}

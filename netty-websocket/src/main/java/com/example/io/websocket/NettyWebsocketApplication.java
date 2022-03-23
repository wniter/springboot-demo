package com.example.io.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yeauty.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class NettyWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyWebsocketApplication.class, args);
    }

}

package com.example.kafka.service;


import com.example.kafka.NettyKafkaApplication;
import com.example.kafka.helloworld.Callback;
import com.example.kafka.helloworld.KafkaEventOne;
import com.example.kafka.helloworld.KafkaEventTwo;

import com.example.kafka.kafka.Sender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService{
    public Map<String, Object> run() {
        Map<String, Object> result = new HashMap<>();

        Callback request = Callback.newBuilder()
                .setOneEvent(KafkaEventOne.newBuilder().setAddress("beijing").build())
                .setTwoEvent(KafkaEventTwo.newBuilder().setNumber(123456).build())
                .build();

        Sender sender = NettyKafkaApplication.context.getBean(Sender.class);
        sender.send(request);

        return result;
    }
}

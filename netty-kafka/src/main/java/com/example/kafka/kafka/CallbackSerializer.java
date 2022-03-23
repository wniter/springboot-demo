package com.example.kafka.kafka;


import com.example.kafka.helloworld.Callback;
import org.apache.kafka.common.serialization.Serializer;


public class CallbackSerializer extends Adapter implements Serializer<Callback> {
    @Override
    public byte[] serialize(final String topic, final Callback data) {
        return data.toByteArray();
    }
}


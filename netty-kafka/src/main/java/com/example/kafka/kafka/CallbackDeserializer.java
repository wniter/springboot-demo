package com.example.kafka.kafka;

import com.example.kafka.helloworld.Callback;
import com.google.protobuf.InvalidProtocolBufferException;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class CallbackDeserializer extends Adapter implements Deserializer<Callback> {
    @Override
    public Callback deserialize(final String topic, byte[] data) {
        try {
            return Callback.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.info("Received un-parse message exception and skip.");
            return null;
        }
    }
}

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: info.proto

package com.example.kafka.helloworld;

public interface CallbackOrBuilder extends
    // @@protoc_insertion_point(interface_extends:helloworld.Callback)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.helloworld.KafkaEventOne one_event = 1;</code>
   */
  boolean hasOneEvent();
  /**
   * <code>.helloworld.KafkaEventOne one_event = 1;</code>
   */
  KafkaEventOne getOneEvent();
  /**
   * <code>.helloworld.KafkaEventOne one_event = 1;</code>
   */
  KafkaEventOneOrBuilder getOneEventOrBuilder();

  /**
   * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
   */
  boolean hasTwoEvent();
  /**
   * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
   */
  KafkaEventTwo getTwoEvent();
  /**
   * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
   */
  KafkaEventTwoOrBuilder getTwoEventOrBuilder();

  public Callback.EventTypeCase getEventTypeCase();
}
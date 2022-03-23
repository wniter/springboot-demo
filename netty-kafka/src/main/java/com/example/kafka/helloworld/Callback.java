// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: info.proto

package com.example.kafka.helloworld;

/**
 * Protobuf type {@code helloworld.Callback}
 */
public  final class Callback extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:helloworld.Callback)
    CallbackOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Callback.newBuilder() to construct.
  private Callback(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Callback() {
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Callback(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            KafkaEventOne.Builder subBuilder = null;
            if (eventTypeCase_ == 1) {
              subBuilder = ((KafkaEventOne) eventType_).toBuilder();
            }
            eventType_ =
                input.readMessage(KafkaEventOne.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((KafkaEventOne) eventType_);
              eventType_ = subBuilder.buildPartial();
            }
            eventTypeCase_ = 1;
            break;
          }
          case 18: {
            KafkaEventTwo.Builder subBuilder = null;
            if (eventTypeCase_ == 2) {
              subBuilder = ((KafkaEventTwo) eventType_).toBuilder();
            }
            eventType_ =
                input.readMessage(KafkaEventTwo.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((KafkaEventTwo) eventType_);
              eventType_ = subBuilder.buildPartial();
            }
            eventTypeCase_ = 2;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return HelloWorldProto.internal_static_helloworld_Callback_descriptor;
  }

  @Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return HelloWorldProto.internal_static_helloworld_Callback_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            Callback.class, Builder.class);
  }

  private int eventTypeCase_ = 0;
  private Object eventType_;
  public enum EventTypeCase
      implements com.google.protobuf.Internal.EnumLite {
    ONE_EVENT(1),
    TWO_EVENT(2),
    EVENTTYPE_NOT_SET(0);
    private final int value;
    private EventTypeCase(int value) {
      this.value = value;
    }
    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static EventTypeCase valueOf(int value) {
      return forNumber(value);
    }

    public static EventTypeCase forNumber(int value) {
      switch (value) {
        case 1: return ONE_EVENT;
        case 2: return TWO_EVENT;
        case 0: return EVENTTYPE_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public EventTypeCase
  getEventTypeCase() {
    return EventTypeCase.forNumber(
        eventTypeCase_);
  }

  public static final int ONE_EVENT_FIELD_NUMBER = 1;
  /**
   * <code>.helloworld.KafkaEventOne one_event = 1;</code>
   */
  public boolean hasOneEvent() {
    return eventTypeCase_ == 1;
  }
  /**
   * <code>.helloworld.KafkaEventOne one_event = 1;</code>
   */
  public KafkaEventOne getOneEvent() {
    if (eventTypeCase_ == 1) {
       return (KafkaEventOne) eventType_;
    }
    return KafkaEventOne.getDefaultInstance();
  }
  /**
   * <code>.helloworld.KafkaEventOne one_event = 1;</code>
   */
  public KafkaEventOneOrBuilder getOneEventOrBuilder() {
    if (eventTypeCase_ == 1) {
       return (KafkaEventOne) eventType_;
    }
    return KafkaEventOne.getDefaultInstance();
  }

  public static final int TWO_EVENT_FIELD_NUMBER = 2;
  /**
   * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
   */
  public boolean hasTwoEvent() {
    return eventTypeCase_ == 2;
  }
  /**
   * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
   */
  public KafkaEventTwo getTwoEvent() {
    if (eventTypeCase_ == 2) {
       return (KafkaEventTwo) eventType_;
    }
    return KafkaEventTwo.getDefaultInstance();
  }
  /**
   * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
   */
  public KafkaEventTwoOrBuilder getTwoEventOrBuilder() {
    if (eventTypeCase_ == 2) {
       return (KafkaEventTwo) eventType_;
    }
    return KafkaEventTwo.getDefaultInstance();
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (eventTypeCase_ == 1) {
      output.writeMessage(1, (KafkaEventOne) eventType_);
    }
    if (eventTypeCase_ == 2) {
      output.writeMessage(2, (KafkaEventTwo) eventType_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (eventTypeCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (KafkaEventOne) eventType_);
    }
    if (eventTypeCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (KafkaEventTwo) eventType_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof Callback)) {
      return super.equals(obj);
    }
    Callback other = (Callback) obj;

    if (!getEventTypeCase().equals(other.getEventTypeCase())) return false;
    switch (eventTypeCase_) {
      case 1:
        if (!getOneEvent()
            .equals(other.getOneEvent())) return false;
        break;
      case 2:
        if (!getTwoEvent()
            .equals(other.getTwoEvent())) return false;
        break;
      case 0:
      default:
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    switch (eventTypeCase_) {
      case 1:
        hash = (37 * hash) + ONE_EVENT_FIELD_NUMBER;
        hash = (53 * hash) + getOneEvent().hashCode();
        break;
      case 2:
        hash = (37 * hash) + TWO_EVENT_FIELD_NUMBER;
        hash = (53 * hash) + getTwoEvent().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static Callback parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Callback parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Callback parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Callback parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Callback parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Callback parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Callback parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Callback parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static Callback parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static Callback parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static Callback parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Callback parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(Callback prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code helloworld.Callback}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:helloworld.Callback)
      CallbackOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return HelloWorldProto.internal_static_helloworld_Callback_descriptor;
    }

    @Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return HelloWorldProto.internal_static_helloworld_Callback_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Callback.class, Builder.class);
    }

    // Construct using com.example.kafka.helloworld.Callback.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      eventTypeCase_ = 0;
      eventType_ = null;
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return HelloWorldProto.internal_static_helloworld_Callback_descriptor;
    }

    @Override
    public Callback getDefaultInstanceForType() {
      return Callback.getDefaultInstance();
    }

    @Override
    public Callback build() {
      Callback result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public Callback buildPartial() {
      Callback result = new Callback(this);
      if (eventTypeCase_ == 1) {
        if (oneEventBuilder_ == null) {
          result.eventType_ = eventType_;
        } else {
          result.eventType_ = oneEventBuilder_.build();
        }
      }
      if (eventTypeCase_ == 2) {
        if (twoEventBuilder_ == null) {
          result.eventType_ = eventType_;
        } else {
          result.eventType_ = twoEventBuilder_.build();
        }
      }
      result.eventTypeCase_ = eventTypeCase_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof Callback) {
        return mergeFrom((Callback)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(Callback other) {
      if (other == Callback.getDefaultInstance()) return this;
      switch (other.getEventTypeCase()) {
        case ONE_EVENT: {
          mergeOneEvent(other.getOneEvent());
          break;
        }
        case TWO_EVENT: {
          mergeTwoEvent(other.getTwoEvent());
          break;
        }
        case EVENTTYPE_NOT_SET: {
          break;
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Callback parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (Callback) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int eventTypeCase_ = 0;
    private Object eventType_;
    public EventTypeCase
        getEventTypeCase() {
      return EventTypeCase.forNumber(
          eventTypeCase_);
    }

    public Builder clearEventType() {
      eventTypeCase_ = 0;
      eventType_ = null;
      onChanged();
      return this;
    }


    private com.google.protobuf.SingleFieldBuilderV3<
        KafkaEventOne, KafkaEventOne.Builder, KafkaEventOneOrBuilder> oneEventBuilder_;
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public boolean hasOneEvent() {
      return eventTypeCase_ == 1;
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public KafkaEventOne getOneEvent() {
      if (oneEventBuilder_ == null) {
        if (eventTypeCase_ == 1) {
          return (KafkaEventOne) eventType_;
        }
        return KafkaEventOne.getDefaultInstance();
      } else {
        if (eventTypeCase_ == 1) {
          return oneEventBuilder_.getMessage();
        }
        return KafkaEventOne.getDefaultInstance();
      }
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public Builder setOneEvent(KafkaEventOne value) {
      if (oneEventBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        eventType_ = value;
        onChanged();
      } else {
        oneEventBuilder_.setMessage(value);
      }
      eventTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public Builder setOneEvent(
        KafkaEventOne.Builder builderForValue) {
      if (oneEventBuilder_ == null) {
        eventType_ = builderForValue.build();
        onChanged();
      } else {
        oneEventBuilder_.setMessage(builderForValue.build());
      }
      eventTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public Builder mergeOneEvent(KafkaEventOne value) {
      if (oneEventBuilder_ == null) {
        if (eventTypeCase_ == 1 &&
            eventType_ != KafkaEventOne.getDefaultInstance()) {
          eventType_ = KafkaEventOne.newBuilder((KafkaEventOne) eventType_)
              .mergeFrom(value).buildPartial();
        } else {
          eventType_ = value;
        }
        onChanged();
      } else {
        if (eventTypeCase_ == 1) {
          oneEventBuilder_.mergeFrom(value);
        }
        oneEventBuilder_.setMessage(value);
      }
      eventTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public Builder clearOneEvent() {
      if (oneEventBuilder_ == null) {
        if (eventTypeCase_ == 1) {
          eventTypeCase_ = 0;
          eventType_ = null;
          onChanged();
        }
      } else {
        if (eventTypeCase_ == 1) {
          eventTypeCase_ = 0;
          eventType_ = null;
        }
        oneEventBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public KafkaEventOne.Builder getOneEventBuilder() {
      return getOneEventFieldBuilder().getBuilder();
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    public KafkaEventOneOrBuilder getOneEventOrBuilder() {
      if ((eventTypeCase_ == 1) && (oneEventBuilder_ != null)) {
        return oneEventBuilder_.getMessageOrBuilder();
      } else {
        if (eventTypeCase_ == 1) {
          return (KafkaEventOne) eventType_;
        }
        return KafkaEventOne.getDefaultInstance();
      }
    }
    /**
     * <code>.helloworld.KafkaEventOne one_event = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        KafkaEventOne, KafkaEventOne.Builder, KafkaEventOneOrBuilder>
        getOneEventFieldBuilder() {
      if (oneEventBuilder_ == null) {
        if (!(eventTypeCase_ == 1)) {
          eventType_ = KafkaEventOne.getDefaultInstance();
        }
        oneEventBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            KafkaEventOne, KafkaEventOne.Builder, KafkaEventOneOrBuilder>(
                (KafkaEventOne) eventType_,
                getParentForChildren(),
                isClean());
        eventType_ = null;
      }
      eventTypeCase_ = 1;
      onChanged();;
      return oneEventBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        KafkaEventTwo, KafkaEventTwo.Builder, KafkaEventTwoOrBuilder> twoEventBuilder_;
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public boolean hasTwoEvent() {
      return eventTypeCase_ == 2;
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public KafkaEventTwo getTwoEvent() {
      if (twoEventBuilder_ == null) {
        if (eventTypeCase_ == 2) {
          return (KafkaEventTwo) eventType_;
        }
        return KafkaEventTwo.getDefaultInstance();
      } else {
        if (eventTypeCase_ == 2) {
          return twoEventBuilder_.getMessage();
        }
        return KafkaEventTwo.getDefaultInstance();
      }
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public Builder setTwoEvent(KafkaEventTwo value) {
      if (twoEventBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        eventType_ = value;
        onChanged();
      } else {
        twoEventBuilder_.setMessage(value);
      }
      eventTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public Builder setTwoEvent(
        KafkaEventTwo.Builder builderForValue) {
      if (twoEventBuilder_ == null) {
        eventType_ = builderForValue.build();
        onChanged();
      } else {
        twoEventBuilder_.setMessage(builderForValue.build());
      }
      eventTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public Builder mergeTwoEvent(KafkaEventTwo value) {
      if (twoEventBuilder_ == null) {
        if (eventTypeCase_ == 2 &&
            eventType_ != KafkaEventTwo.getDefaultInstance()) {
          eventType_ = KafkaEventTwo.newBuilder((KafkaEventTwo) eventType_)
              .mergeFrom(value).buildPartial();
        } else {
          eventType_ = value;
        }
        onChanged();
      } else {
        if (eventTypeCase_ == 2) {
          twoEventBuilder_.mergeFrom(value);
        }
        twoEventBuilder_.setMessage(value);
      }
      eventTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public Builder clearTwoEvent() {
      if (twoEventBuilder_ == null) {
        if (eventTypeCase_ == 2) {
          eventTypeCase_ = 0;
          eventType_ = null;
          onChanged();
        }
      } else {
        if (eventTypeCase_ == 2) {
          eventTypeCase_ = 0;
          eventType_ = null;
        }
        twoEventBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public KafkaEventTwo.Builder getTwoEventBuilder() {
      return getTwoEventFieldBuilder().getBuilder();
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    public KafkaEventTwoOrBuilder getTwoEventOrBuilder() {
      if ((eventTypeCase_ == 2) && (twoEventBuilder_ != null)) {
        return twoEventBuilder_.getMessageOrBuilder();
      } else {
        if (eventTypeCase_ == 2) {
          return (KafkaEventTwo) eventType_;
        }
        return KafkaEventTwo.getDefaultInstance();
      }
    }
    /**
     * <code>.helloworld.KafkaEventTwo two_event = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        KafkaEventTwo, KafkaEventTwo.Builder, KafkaEventTwoOrBuilder>
        getTwoEventFieldBuilder() {
      if (twoEventBuilder_ == null) {
        if (!(eventTypeCase_ == 2)) {
          eventType_ = KafkaEventTwo.getDefaultInstance();
        }
        twoEventBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            KafkaEventTwo, KafkaEventTwo.Builder, KafkaEventTwoOrBuilder>(
                (KafkaEventTwo) eventType_,
                getParentForChildren(),
                isClean());
        eventType_ = null;
      }
      eventTypeCase_ = 2;
      onChanged();;
      return twoEventBuilder_;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:helloworld.Callback)
  }

  // @@protoc_insertion_point(class_scope:helloworld.Callback)
  private static final Callback DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new Callback();
  }

  public static Callback getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Callback>
      PARSER = new com.google.protobuf.AbstractParser<Callback>() {
    @Override
    public Callback parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Callback(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Callback> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<Callback> getParserForType() {
    return PARSER;
  }

  @Override
  public Callback getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


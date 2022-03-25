package com.example.netty.decoder;

/**
 * Netty核心组件：Decoder与Encoder
 *Netty从底层Java通道读到ByteBuf二进制数据，传入Netty 通道的流水线，随后开始入
 * 站处理。在入站处理过程中，需要将ByteBuf二进制类型，解码成Java POJO对象。这个解
 * 码过程，可以通过Netty的Decoder（解码器）去完成。
 *在出站处理过程中，业务处理后的结果（出站数据），需要从某个Java POJO对象，编
 * 码为最终的ByteBuf二进制数据，然后通过底层Java通道发送到对端。在编码过程中，需要
 * 用到Netty的Encoder（编码器）去完成数据的编码工作。
 *
 * Decoder原理与实践
 * 原理：ByteToMessageDecoder解码器处理流程
 *ByteToMessageDecoder是一个非常重要的解码器基类，它是一个抽象类，实现了解码
 * 处理的基础逻辑和流程。ByteToMessageDecoder继承自ChannelInboundHandlerAdapter适配
 * 器，是一个入站处理器，用于完成从ByteBuf到Java POJO对象的解码功能。
 * ByteToMessageDecoder解码的流程，具体可以描述为：首先，它将上一站传过来的输
 * 入到Bytebuf中的数据进行解码，解码出一个List<Object>对象列表；然后，迭代
 * List<Object>列表，逐个将Java POJO对象传入下一站Inbound入站处理器。
 * 如果要实现一个自己的解码器，首先继承ByteToMessageDecoder抽象类。然后，实现
 * 其基类的decode抽象方法，总体来说，流程大致如下：
 * （1）首先继承ByteToMessageDecoder抽象类。
 * （2）然后实现其基类的decode抽象方法，将ByteBuf到目标POJO解码逻辑写入此方
 * 法，负责将Bytebuf中的二进制数据，解码成一个一个的Java POJO对象。
 * （3）解码完成后，需要将解码后的Java POJO对象，放入decode方法的List<Object>实
 * 参中，此实参是父类所传入的解码结果收集容器。
 * 余下的工作，都有父类ByteToMessageDecoder去自动完成。在流水线的处理过程中，
 * 父类在执行完子类的decode解码后，会将List<Object>收集到的结果，一个一个地、逐个传
 * 递到下一个Inbound入站处理器.
 */
//public class DecoderAndEncoder {
//}

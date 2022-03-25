package com.example.netty.protocol;


import com.example.common.util.Logger;
import com.example.netty.NettyDemoConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * Protobuf传输之服务器端的案例
 * 在服务器端，Protobuf协议的解码过程如下：
 * 先使用Netty内置的ProtobufVarint32FrameDecoder，根据varint32格式的可变长度值，从
 * 入站数据包中解码出二进制Protobuf字节码。然后，可以使用Netty内置的ProtobufDecoder
 * 解码器将字节码解码成Protobuf POJO对象。最后，自定义一个ProtobufBussinessDecoder解
 * 码器来处理Protobuf POJO对象。
 */

public class ProtoBufServer {

    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public ProtoBufServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        //创建reactor 线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(bossLoopGroup, workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioServerSocketChannel.class);
            //3 设置监听端口
            b.localAddress(serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加3个handler处理器
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(MsgProtos.Msg.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufBussinessDecoder());
                }
            });
            // 6 开始绑定server
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            Logger.info(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());

            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }

    }

    //服务器端业务处理器
    static class ProtobufBussinessDecoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            MsgProtos.Msg protoMsg = (MsgProtos.Msg) msg;
            //经过pipeline的各个decoder，到此Person类型已经可以断定
            Logger.info("收到一个 MsgProtos.Msg 数据包 =》");
            Logger.info("protoMsg.getId():=" + protoMsg.getId());
            Logger.info("protoMsg.getContent():=" + protoMsg.getContent());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        int port = NettyDemoConfig.SOCKET_SERVER_PORT;
        new ProtoBufServer(port).runServer();
    }


}
/**
 Netty内置的Protobuf基础编码器/解码器
 Netty内置的基础Protobuf编码器/解码器为：ProtobufEncoder编码器和ProtobufDecoder
 解码器。此外，还提供了一组简单的解决半包问题的编码器和解码器。
 1. ProtobufEncoder编码器
 翻开Netty源代码，我们发现ProtobufEncoder的实现逻辑非常简单，直接使用了
 Protobuf POJO实例的toByteArray()方法将自身编码成二进制字节，然后放入Netty的
 Bytebuf缓冲区中，接着会被发送下一站编码器。其源码如下：
 package io.netty.handler.codec.protobuf;
 ....
 @Sharable
 public class ProtobufEncoder extends
 MessageToMessageEncoder<MessageLiteOrBuilder> {
 @Override
 protected void encode(ChannelHandlerContext ctx,
 MessageLiteOrBuilder msg, List<Object> out)
 throws Exception {
 if (msg instanceof MessageLite) {
 out.add(Unpooled.wrappedBuffer(
 ((MessageLite) msg).toByteArray()));
 return;
 }
 if (msg instanceof MessageLite.Builder) {
 out.add(Unpooled.wrappedBuffer((
 (MessageLite.Builder) msg).build().toByteArray()));
 }
 } }
 2. ProtobufDecoder解码器
 ProtobufDecoder和ProtobufEncoder相互对应，只不过在使用的时候，ProtobufDecoder
 解码器需要指定一个Protobuf POJO实例，作为解码的参考原型（prototype），解码时会根
 据原型实例找到对应的Parser解析器，将二进制的字节解码为Protobuf POJO实例。
 new ProtobufDecoder(MsgProtos.Msg.getDefaultInstance())
 在Java NIO通信中，仅仅使用以上这组编码器和解码器，传输过程中会存在粘包/半包
 的问题。Netty也提供了配套的Head-Content类型的Protobuf编码器和解码器，在二进制码流
 之前加上二进制字节数组的长度。
 3. ProtobufVarint32LengthFieldPrepender长度编码器
 这个编码器的作用是，在ProtobufEncoder生成的字节数组之前，前置一个varint32数
 字，表示序列化的二进制字节数量或者长度。
 4. ProtobufVarint32FrameDecoder长度解码器
 ProtobufVarint32FrameDecoder和ProtobufVarint32LengthFieldPrepender相互对应，其作
 用是，根据数据包中长度域（varint32类型）中的长度值，解码一个足额的字节数组，然后
 将字节数组交给下一站的解码器ProtobufDecoder。
 什么是varint32类型的长度？ Protobuf为什么不用int这种固定类型的长度呢?
 varint32是一种紧凑的表示数字的方法，它不是一种固定长度（如32位）的数字类型。
 varint32它用一个或多个字节来表示一个数字，值越小的数字，使用的字节数越少，值越大
 使用的字节数越多。varint32根据值的大小自动进行收缩，这能减少用于保存长度的字节
 数。也就是说，varint32与int类型的最大区别是：varint32用一个或多个字节来表示一个数
 字，而int是固定长度的数字。varint32不是固定长度，所以为了更好地减少通信过程中的传
 输量，消息头中的长度尽量采用varint格式。
 */
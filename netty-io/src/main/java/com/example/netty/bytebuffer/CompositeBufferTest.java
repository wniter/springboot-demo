package com.example.netty.bytebuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;


/**
 * Netty的零拷贝（Zero-Copy）
 * （1）Netty提供CompositeByteBuf组合缓冲区类, 可以将多个ByteBuf合并为一个逻辑
 * 上的ByteBuf, 避免了各个ByteBuf之间的拷贝。
 * （2）Netty提供了ByteBuf的浅层复制操作（slice、duplicate），可以将ByteBuf分解为
 * 多个共享同一个存储区域的ByteBuf, 避免内存的拷贝。
 * （3）在使用Netty进行文件传输时，可以调用FileRegion包装的transferTo方法，直接将
 * 文件缓冲区的数据发送到目标Channel，避免普通的循环读取文件数据和写入通道所导致的
 * 内存拷贝问题。
 * （4）在将一个byte数组转换为一个ByteBuf对象的场景，Netty提供了一系列的包装
 * 类，避免了转换过程中的内存拷贝。
 * （5）如果Channel接收和发送ByteBuf都使用direct直接内存进行Socket读写，不需要进
 * 行缓冲区的二次拷贝。但是，如果使用JVM的堆内存进行Socket读写，JVM会将堆内存
 * Buffer拷贝一份到直接内存中，然后才写入Socket中，相比于使用直接内存，这种情况在发
 * 送过程中会多出一次缓冲区的内存拷贝。所以，在发送ByteBuffer到Socket时，尽量使用直
 * 接内存而不是JVM堆内存。
 *
 */
public class CompositeBufferTest {
    static Charset utf8 = Charset.forName("UTF-8");

    /**
     * 通过CompositeByteBuf实现零拷贝
     * 通过wrap操作实现零拷贝
     * byte[] bytes = ...
     * ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
     */
    @Test
    public void intCompositeBufComposite() {
        CompositeByteBuf cbuf = Unpooled.compositeBuffer(3);
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{1, 2, 3}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{4}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{5, 6}));
        //合并成一个单独的缓冲区
        ByteBuffer nioBuffer = cbuf.nioBuffer(0, 6);
        byte[] bytes = nioBuffer.array();
        System.out.print("bytes = ");
        for (byte b : bytes) {
            System.out.print(b);
        }
        cbuf.release();
    }

    @Test
    public void byteBufComposite() {
        CompositeByteBuf cbuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息头
        ByteBuf headerBuf = Unpooled.copiedBuffer("疯狂创客圈:", utf8);
        //消息体1
        ByteBuf bodyBuf = Unpooled.copiedBuffer("高性能 Netty", utf8);
        cbuf.addComponents(headerBuf, bodyBuf);
        sendMsg(cbuf);
        headerBuf.retain();
        cbuf.release();

        cbuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息体2
        bodyBuf = Unpooled.copiedBuffer("高性能学习社群", utf8);
        cbuf.addComponents(headerBuf, bodyBuf);
        sendMsg(cbuf);
        cbuf.release();
    }

    private void sendMsg(CompositeByteBuf cbuf) {
        //处理整个消息
        for (ByteBuf b : cbuf) {
            int length = b.readableBytes();
            byte[] array = new byte[length];
            //将CompositeByteBuf中的数据复制到数组中
            b.getBytes(b.readerIndex(), array);
            //处理一下数组中的数据
            System.out.print(new String(array, utf8));
        }
        System.out.println();
    }

}
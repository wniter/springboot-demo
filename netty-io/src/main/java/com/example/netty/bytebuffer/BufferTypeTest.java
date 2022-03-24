package com.example.netty.bytebuffer;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * 首先对比介绍一下Heap ByteBuf和Direct ByteBuf两类缓冲区的使用。它们有以下几点
 * 不同：
 * ⚫ 创建的方法不同：Heap ByteBuf通过调用分配器的buffer()方法来创建；而Direct
 * ByteBuf的创建，是通过调用分配器的directBuffer()方法。
 * ⚫ Heap ByteBuf缓冲区可以直接通过array()方法读取内部数组；而Direct ByteBuf缓冲
 * 区不能读取内部数组。
 * ⚫ 可以调用hasArray()方法来判断是否为Heap ByteBuf类型的缓冲区；如果hasArray()
 * 返回值为true，则表示是Heap堆缓冲，否则为直接内存缓冲区。
 * ⚫ 如果要从Direct ByteBuf读取缓冲数据进行Java程序处理时，会相对比较麻烦，需
 * 要通过getBytes/readBytes等方法先将数据复制到Java的堆内存，然后进行其他的计
 * 算
 */
public class BufferTypeTest {
    final static Charset UTF_8 = Charset.forName("UTF-8");

    //堆缓冲区
    @Test
    public  void testHeapBuffer() {
        //取得堆内存
        ByteBuf heapBuf =  ByteBufAllocator.DEFAULT.buffer();
        heapBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (heapBuf.hasArray()) {
            //取得内部数组
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            Logger.info(new String(array,offset,length, UTF_8));
        }
        heapBuf.release();

    }

    //直接缓冲区
    @Test
    public  void testDirectBuffer() {
        ByteBuf directBuf =  ByteBufAllocator.DEFAULT.directBuffer();
        directBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            //读取数据到堆内存
            directBuf.getBytes(directBuf.readerIndex(), array);
            Logger.info(new String(array, UTF_8));
        }
        directBuf.release();
    }
}

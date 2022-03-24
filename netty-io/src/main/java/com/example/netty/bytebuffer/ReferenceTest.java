package com.example.netty.bytebuffer;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * JVM中使用“计数器”（一种GC算法）来标记对象是否“不可达”进而收回（注：
 * GC是Garbage Collection的缩写，即Java中的垃圾回收机制），Netty也使用了这种手段来对
 * ByteBuf的引用进行计数，Netty的ByteBuf的内存回收工作是通过引用计数的方式管理的。
 * Netty之所以采用“计数器”来追踪ByteBuf的生命周期，一是能对Pooled ByteBuf的支
 * 持，二是能够尽快地“发现”那些可以回收的ByteBuf（非Pooled），以便提升ByteBuf的
 * 分配和销毁的效率。
 *
 * ByteBuf引用计数的大致规则如下：在默认情况下，当创建完一个ByteBuf时，它的引
 * 用为1；每次调用retain()方法，它的引用就加1；每次调用release()方法，就是将引用计数减
 * 1；如果引用为0，再次访问这个ByteBuf对象，将会抛出异常；如果引用为0，表示这个
 * ByteBuf没有哪个进程引用它，它占用的内存需要回收。
 *
 *
 */
public class ReferenceTest {
    /**
     * 当ByteBuf的引用计数已经为0，Netty会进行ByteBuf的回收。分为两种场景：
     * （1）如果属于Pooled池化的ByteBuf内存，回收方法是：放入可以重新分配的ByteBuf
     * 池子，等待下一次分配；
     * （2）Unpooled未池化的ByteBuf缓冲区，需要细分为两种情况：如果是堆（Heap）结
     * 构缓冲，会被JVM的垃圾回收机制回收；如果是Direct直接内存的类型，则会调用本地方法
     * 释放外部内存（unsafe.freeMemory）。
     */
    @Test
    public  void testRef()
    {

        ByteBuf buffer  = ByteBufAllocator.DEFAULT.buffer();
        Logger.info("after create:"+buffer.refCnt());
        buffer.retain();
        Logger.info("after retain:"+buffer.refCnt());
        buffer.release();
        Logger.info("after release:"+buffer.refCnt());
        buffer.release();
        Logger.info("after release:"+buffer.refCnt());
        //错误:refCnt: 0,不能再retain
        buffer.retain();
        Logger.info("after retain:"+buffer.refCnt());
    }
    /**
     * [main|ReferenceTest.testRef] |>  after create:1
     * [main|ReferenceTest.testRef] |>  after retain:2
     * [main|ReferenceTest.testRef] |>  after release:1
     * [main|ReferenceTest.testRef] |>  after release:0
     */
}
/**
 Netty通过ByteBufAllocator分配器来创建缓冲区和分配内存空间。Netty提供了两种分
 配器实现：PoolByteBufAllocator和UnpooledByteBufAllocator。
 PoolByteBufAllocator（池化的ByteBuf分配器）将ByteBuf实例放入池中，提高了性
 能，将内存碎片减少到最小；池化分配器采用了jemalloc高效内存分配的策略，该策略被好
 几种现代操作系统所采用。
 UnpooledByteBufAllocator是普通的未池化ByteBuf分配器，它没有把ByteBuf放入池
 中，每次被调用时，返回一个新的ByteBuf实例；使用完之后，通过Java的垃圾回收机制回
 收或者直接释放（对于直接内存而言）。

 */
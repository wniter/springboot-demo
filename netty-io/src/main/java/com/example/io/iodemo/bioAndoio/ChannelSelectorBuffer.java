package com.example.io.iodemo.bioAndoio;
//
//public class ChannelSelectorBuffer {
//}
/**
 通道（Channel）:
 Channel和Stream的一个显著的不同是：Stream是单向的，譬如InputStream是单向的只
 读流，OutputStream是单向的只写流；而Channel是双向的，既可以用来进行读操作，又可
 以用来进行写操作。
 NIO中的Channel的主要实现有：
 1. FileChannel 用于文件IO操作
 2.DatagramChannel 用于UDP的IO操作
 3.SocketChannel 用于TCP的传输操作
 4.ServerSocketChannel 用于TCP连接监听操作

 选择器（Selector）:
 IO多路复用指的是一个进程/线程可以同时监视多个文件描述符（含socket连接），一
 旦其中的一个或者多个文件描述符可读或者可写，该监听进程/线程能够进行IO事件的查
 询。
 在Java应用层面，如何实现对多个文件描述符的监视呢？
 需要用到一个非常重要的Java NIO组件——Selector 选择器。Selector 选择器可以理解
 为一个IO事件的监听与查询器。通过选择器，一个线程可以查询多个通道的IO事件的就绪
 状态。
 在介绍Selector选择器之前，首先介绍一下这个前置的概念：IO事件。
 什么是IO事件呢？表示通道某种IO操作已经就绪、或者说已经做好了准备。例如，如
 果一个新Channel链接建立成功了，就会在Server Socket Channel上发生一个IO事件，代表一
 个新连接一个准备好，这个IO事件叫做“接收就绪”事件。再例如，一个Channel通道如果
 有数据可读，就会发生一个IO事件，代表该连接数据已经准备好，这个IO事件叫做 “读就
 绪”事件。
 Java NIO将NIO事件进行了简化，只定义了四个事件，这四种事件用SelectionKey的四
 个常量来表示：
 ⚫ SelectionKey.OP_CONNECT
 ⚫ SelectionKey.OP_ACCEPT
 ⚫ SelectionKey.OP_READ
 ⚫ SelectionKey.OP_WRITE

 缓冲区（Buffer）:
 Buffer顾名思义：缓冲区，实际上是一个容器，一个连续数组。Channel提供从文件、
 网络读取数据的渠道，但是读写的数据都必须经过Buffer。

 详解NIO Buffer类及其属性
 Java NIO中代表缓冲区的Buffer类是一个抽象类，位于java.nio包中。
 NIO的Buffer的内部是一个内存块（数组），此类与普通的内存块（Java数组）不同的
 是：NIO Buffer对象，提供了一组比较有效的方法，用来进行写入和读取的交替访问。

 Buffer类
 Buffer类是一个抽象类，对应于Java的主要数据类型，在NIO中有8种缓冲区类，分别
 如下：ByteBuffer、CharBuffer、DoubleBuffer、FloatBuffer、IntBuffer、LongBuffer、
 ShortBuffer、MappedByteBuffer。 前7种Buffer类型，覆盖了能在IO中传输的所有的Java基本数据类型。第8种类型
 MappedByteBuffer是专门用于内存映射的一种ByteBuffer类型。不同的Buffer子类，其能操
 作的数据类型能够通过名称进行判断，比如IntBuffer只能操作Integer类型的对象。
 实际上，使用最多的还是ByteBuffer二进制字节缓冲区类型，后面会看到。
 Buffer类的重要属性
 ⚫ capacity（容量）
 ⚫ position（读写位置）
 ⚫ limit（读写的限制）
 */
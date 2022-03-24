package com.example.netty.basic;
/**
 * 用户程序主要涉及的Handler环节为：数据包解码、业务处理、目标数据编码、把数
 * 据包写到通道中。
 * 前面已经介绍过，从应用程序开发人员的角度来看，有入站和出站两种类型操作。
 * ⚫ 入站处理触发的方向为：自底向上，Netty的内部（如通道）到
 * ChannelInboundHandler入站处理器。
 * ⚫ 出站处理触发的方向为：自顶向下，从ChannelOutboundHandler出站处理器到
 * Netty的内部（如通道）。
 *
 *ChannelInboundHandler入站处理器
 *对于ChannelInboundHandler的核心方法，大致的介绍如下：
 * 1. channelRegistered
 * 当通道注册完成后，Netty会调用fireChannelRegistered方法，触发通道注册事件。而在
 * 通道流水线注册过的入站处理器Handler的channelRegistered回调方法，将会被调用到。
 * 2. channelActive
 *当通道激活完成后，Netty会调用fireChannelActive方法，触发通道激活事件。而在通
 * 道流水线注册过的入站处理器的channelActive回调方法，会被调用到。
 * 3. channelRead
 * 当通道缓冲区可读，Netty会调用fireChannelRead，触发通道可读事件。而在通道流水
 * 线注册过的入站处理器的channelRead回调方法，会被调用到，以便完成入站数据的读取和
 * 处理。
 * 4. channelReadComplete
 * 当通道缓冲区读完，Netty会调用fireChannelReadComplete，触发通道缓冲区读完事
 * 件。而在通道流水线注册过的入站处理器的channelReadComplete回调方法，会被调用到。
 * 5. channelInactive
 * 当连接被断开或者不可用时，Netty会调用fireChannelInactive，触发连接不可用事件。
 * 而在通道流水线注册过的入站处理器的channelInactive回调方法，会被调用到。
 * 6. exceptionCaught
 * 当通道处理过程发生异常时，Netty会调用fireExceptionCaught，触发异常捕获事件。而
 * 在通道流水线注册过的入站处理器的exceptionCaught方法，会被调用到。注意，这个方法
 * 是在通道处理器中ChannelHandler定义的方法，入站处理器、出站处理器接口都继承到了
 * 该方法。
 *
 *
 *ChannelOutboundHandler出站处理器
 *当业务处理完成后，需要操作Java NIO底层通道时，通过一系列的
 * ChannelOutboundHandler出站处理器，完成Netty通道到底层通道的操作。比方说建立底层
 * 连接、断开底层连接、写入底层Java NIO通道等。ChannelOutboundHandler接口定义了大部
 * 分的出站操作，
 *再强调一下，Netty出站处理的方向：是通过上层Netty通道，去操作底层Java IO 通
 * 道。主要出站（Outbound）的操作如下：
 * 1. bind
 *监听地址（IP+端口）绑定：完成底层Java IO通道的IP地址绑定。如果使用TCP传输协
 * 议，这个方法用于服务器端。
 * 2. connect
 * 连接服务端：完成底层Java IO通道的服务器端的连接操作。如果使用TCP传输协议，
 * 这个方法用于客户端。
 * 3. write
 * 写数据到底层：完成Netty通道向底层Java IO 通道的数据写入操作。此方法仅仅是触
 * 发一下操作而已，并不是完成实际的数据写入操作。
 * 4. flush
 * 将底层缓存区的数据腾空，立即写出到对端。
 * 5. read
 * 从底层读数据：完成Netty通道从Java IO通道的数据读取。
 * 6. disConnect
 * 断开服务器连接：断开底层Java IO通道的socket连接。如果使用TCP传输协议，此方法
 * 主要用于客户端。
 * 7. close
 * 主动关闭通道：关闭底层的通道，例如服务器端的新连接监听通道。
 *
 * ChannelInitializer通道初始化处理器
 *  //step5：装配子通道流水线
 * b.childHandler(new ChannelInitializer<SocketChannel>() {
 *  //有连接到达时会创建一个通道的子通道，并初始化
 *  protected void initChannel(SocketChannel ch)...{
 *  // 这里可以管理子通道中的 Handler 业务处理器
 *  // 向子通道流水线添加一个 Handler 业务处理器
 *  ch.pipeline().addLast(new NettyDiscardHandler());
 * }
 * })
 *
 *initChannel()方法的大致业务代码是：拿到新连接通道作为实际参数，往它
 * 的流水线中装配Handler业务处理器。
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */


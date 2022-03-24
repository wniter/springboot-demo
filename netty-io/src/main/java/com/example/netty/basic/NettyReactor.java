package com.example.netty.basic;
//
//public class NettyReactor {
//}
/**
 流程：
 Reactor反应器模式中IO事件的处理流程，大致分为4步，具体如下：
 第1步：通道注册。IO事件源于通道（Channel），IO是和通道（对应于底层连接而
 言）强相关的。一个IO事件一定属于某个通道。但是，如果要查询通道的事件，首先要将
 通道注册到选择器。
 第2步：查询事件。在反应器模式中，一个线程会负责一个反应器（或者SubReactor子 反应器），不断地轮询，查询选择器中的IO事件（选择键）。
 第3步：事件分发。如果查询到IO事件，则分发给与IO事件有绑定关系的Handler业务
 处理器。
 第4步：完成真正的IO操作和业务处理，这一步由Handler业务处理器负责。

 Netty中的Channel通道组件
 总结起来，对应到不同的协议，Netty实现了对应的通道，每一种协议基本上都有NIO
 （异步IO）和OIO（阻塞式IO）两个版本。
 对应于不同的协议，Netty中常见的通道类型如下：
 ⚫ NioSocketChannel：异步非阻塞TCP Socket传输通道。
 ⚫ NioServerSocketChannel：异步非阻塞TCP Socket服务器端监听通道。
 ⚫ NioDatagramChannel：异步非阻塞的UDP传输通道。
 ⚫ NioSctpChannel：异步非阻塞Sctp传输通道。
 ⚫ NioSctpServerChannel：异步非阻塞Sctp服务器端监听通道。
 ⚫ OioSocketChannel：同步阻塞式TCP Socket传输通道。
 ⚫ OioServerSocketChannel：同步阻塞式TCP Socket服务器端监听通道。
 ⚫ OioDatagramChannel：同步阻塞式UDP传输通道。
 ⚫ OioSctpChannel：同步阻塞式Sctp传输通道。
 ⚫ OioSctpServerChannel：同步阻塞式Sctp服务器端监听通道。

 Netty中的Reactor 反应器
 NioEventLoop类有两个重要的成员属性：一个是Thread线程类的成员，一个是Java
 NIO选择器的成员属性。NioEventLoop的继承关系和主要的成员属性，

 Netty中的Handler处理器
 Netty的Handler处理器分为两大类：第一类是ChannelInboundHandler入站处理器；第二
 类是ChannelOutboundHandler出站处理器，二者都继承了ChannelHandler处理器接口。

 Netty的Pipeline通道处理流水线
 （1）反应器（或者SubReactor子反应器）和通道之间是一对多的关系：一个反应器可
 以查询很多个通道的IO事件。
 （2）通道和Handler处理器实例之间，是多对多的关系：一个通道的IO事件可以被多
 个的Handler实例处理；一个Handler处理器实例也能绑定到很多的通道，处理多个通道的IO
 事件。


 */
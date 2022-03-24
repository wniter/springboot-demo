package com.example.netty.pipeline;

//public class ChannelHandlerContext {
//}
/**

 核心类：ChannelHandlerContext处理器上下文
 ChannelHandlerContext通道处理器上下文类。当Handler业务处理器被添加到流水
 线中时，会为其专门创建一个通道处理器上下文ChannelHandlerContext实例，主要封装了
 ChannelHandler通道处理器和ChannelPipeline通道流水线之间的关联关系。
 流水线ChannelPipeline中的双向链接，实质是一个由ChannelHandlerContext组成的
 双向链表。而无状态的Handler，作为Context的成员，关联在ChannelHandlerContext中。

Channel、Handler、ChannelHandlerContext三者的关系：Channel通道拥有一
 条ChannelPipeline通道流水线，每一个流水线节点为一个ChannelHandlerContext上下文对
 象，每一个上下文中包裹了一个ChannelHandler通道处理器。在ChannelHandler通道处理器
 的入站/出站处理方法中，Netty都会传递一个Context上下文实例作为实际参数。处理器中
 的回调代码，可以通过Context实参，在业务处理过程中去获取ChannelPipeline实例或者
 Channel实例
 流水线尾部的TailContext不仅仅是一个上下文类，而且是一个入站处理器类，实现了
 所有入站处理回调方法，这些回调实现的主要工作，基本上都是收尾处理的，如释放缓冲
 区对象、完成异常处理等

 核心原理：Pipeline入站和出站的双向链接操作




























 */
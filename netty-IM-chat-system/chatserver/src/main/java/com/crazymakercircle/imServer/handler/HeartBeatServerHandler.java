package com.crazymakercircle.imServer.handler;

import com.crazymakercircle.cocurrent.FutureTaskScheduler;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 *什么是连接假死呢？如果底层的TCP连接（Socket连接）已经断开，但是服务器端并
 * 没有正常地关闭Socket套接字，服务器端认为这条TCP连接仍然是存在的，则该连接处于
 * “假死”状态。连接假死的具体表现如下：
 * （1）在服务器端，会有一些处于TCP_ESTABLISHED状态的“正常”连接；
 * （2）但在客户端，TCP客户端已经显示连接已经断开；
 * （3）客户端此时虽然可以进行断线重连操作，但是上一次的连接状态依然被服务器端
 * 认为有效，并且服务器端的资源得不到正确释放，包括套接字上下文以及接收/发送缓冲
 * 区。
 *连接假死的情况虽然不多见，但是确实存在。服务器端长时间运行后，会面临大量假
 * 死连接得不到正常释放的情况。由于每个连接都会耗费CPU和内存资源，因此大量假死的
 * 连接会逐渐耗光服务器的资源，使得服务器越来越慢，IO处理效率越来越低，最终导致服
 * 务器崩溃。
 * 连接假死通常是由以下多个原因造成的，例如：
 * （1）应用程序出现线程堵塞，无法进行数据的读写；
 * （2）网络相关的设备出现故障，例如网卡、机房故障；
 * （3）网络丢包。公网环境非常容易出现丢包和网络抖动等现象；
 * 解决假死的有效手段是：客户端定时进行心跳检测，服务器端定时进行空闲检测。
 */
@Slf4j
public class HeartBeatServerHandler extends IdleStateHandler {

    private static final int READ_IDLE_GAP = 150;

    public HeartBeatServerHandler() {
        super(READ_IDLE_GAP, 0, 0, TimeUnit.SECONDS);

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        //判断消息类型
        ProtoMsg.HeadType headType = pkg.getType();
        if (headType.equals(ProtoMsg.HeadType.HEART_BEAT)) {
            //异步处理,将心跳包，直接回复给客户端
            FutureTaskScheduler.add(() -> {
                if (ctx.channel().isActive()) {
                    ctx.writeAndFlush(msg);
                }
            });

        }
        super.channelRead(ctx, msg);

    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READ_IDLE_GAP + "秒内未读到数据，关闭连接");
        ServerSession.closeSession(ctx);
    }
}
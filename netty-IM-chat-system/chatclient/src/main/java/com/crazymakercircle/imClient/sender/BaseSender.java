package com.crazymakercircle.imClient.sender;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *在Netty中，无论是出站操作，还是出站操作，都有两大的特点：
 * （1）同一条通道的同一个Handler处理器的所有出/入站处理都是串行的，而不是并行
 * 的。Netty是如何保障这一点的呢？在某个出/入站开启时，Netty会对当前的执行线程进行
 * 判断：如果当前线程不是Handler的执行线程，则处理暂时不执行，Netty会为当前处理建立
 * 一个新的异步可执行任务，加入到Handler的执行线程的任务队列中。
 *（2）Netty的出/入站操作不是单个Handler业务处理器操作，而是流水线上的一系列的
 * 出/入站处理流程。只有整个流程都处理完，出/入站操作才真正处理完成。
 * 基于以上两点，大家可以简单地推断，在调用完channel.writeAndFlush(pkg) 后，真正
 * 的出站操作肯定是没有执行完成的，可能还需要在EventLoop的任务队列中排队等待。
 * 如何才能判断writeAndFlush()执行完毕了呢？writeAndFlush()方法会返回一个
 * ChannelFuture异步任务实例，可以通过为其增加GenericFutureListener监听器的方式，来判
 * 断writeAndFlush()是否已经执行完毕。当监听器的operationComplete方法被回调时，表示
 * writeAndFlush()方法已经执行完毕了。而具体的回调业务逻辑，可以放在operationComplete
 * 回调方法中。
 */
@Data
@Slf4j
public abstract class BaseSender {


    private User user;
    private ClientSession session;


    public boolean isConnected() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isConnected();
    }

    public boolean isLogin() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

    public void sendMsg(ProtoMsg.Message message) {


        if (null == getSession() || !isConnected()) {
            log.info("连接还没成功");
            return;
        }

        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(message);
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future)
                    throws Exception {
                // 回调
                if (future.isSuccess()) {
                    sendSucced(message);
                } else {
                    sendfailed(message);

                }
            }

        });



/*

        try {
            f.sync();
        } catch (InterruptedException e) {

            e.printStackTrace();
            sendException(message);
        }

*/
    }

    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功");

    }

    protected void sendfailed(ProtoMsg.Message message) {
        log.info("发送失败");
    }


}

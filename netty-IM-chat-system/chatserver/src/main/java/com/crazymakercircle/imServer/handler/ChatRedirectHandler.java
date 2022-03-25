package com.crazymakercircle.imServer.handler;

import com.crazymakercircle.cocurrent.FutureTaskScheduler;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.processer.ChatRedirectProcesser;
import com.crazymakercircle.imServer.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务器端的ChatRedirectHandler消息转发
 *服务器端收到聊天消息后，会进行消息的转发，主要由消息转发处理器
 * ChatRedirectHandler负责，其大致的工作如下：
 * （1）对消息类型进行判断：判断是否为聊天请求Protobuf数据包。如果不是，通过调
 * 用super.channelRead(ctx, msg) 将消息交给流水线的下一站；
 * （2）对消息发送方用户登录进行判断：如果没有登录，则不能发送消息；
 * （3）开启异步的消息转发，由其ChatRedirectProcesser实例负责完成消息转发。
 */
@Slf4j
@Service("ChatRedirectHandler")
@ChannelHandler.Sharable
public class ChatRedirectHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    ChatRedirectProcesser chatRedirectProcesser;

    /**
     * 收到消息
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断消息类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message) msg).getType();
        if (!headType.equals(chatRedirectProcesser.type())) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断是否登录
        ServerSession session = ServerSession.getSession(ctx);
        if (null == session || !session.isLogin()) {
            log.error("用户尚未登录，不能发送消息");
            return;
        }

        //异步处理IM消息转发的逻辑
        FutureTaskScheduler.add(() ->
        {
            chatRedirectProcesser.action(session, pkg);
        });


    }

}

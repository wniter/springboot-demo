package com.crazymakercircle.imClient.handler;


import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Service;

/**
 * 客户端的ChatMsgHandler聊天消息处理器很简单，主要的工作如下：
 * （1）对消息类型进行判断：判断是否为聊天请求Protobuf数据包。如果不是，通过
 * super.channelRead(ctx, msg) 将消息交给流水线的下一站。
 * （2）如果是聊天消息，则将聊天消息显示在控制台.
 *
 */
@ChannelHandler.Sharable
@Service("ChatMsgHandler")
public class ChatMsgHandler extends ChannelInboundHandlerAdapter {


    public ChatMsgHandler() {

    }


    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (!headType.equals(ProtoMsg.HeadType.MESSAGE_REQUEST)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.MessageRequest req = pkg.getMessageRequest();
        String content = req.getContent();
        String uid = req.getFrom();

        System.out.println(" 收到消息 from uid:" + uid + " -> " + content);
    }


}

package com.crazymakercircle.imClient.handler;


import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import com.crazymakercircle.imClient.protoBuilder.HeartBeatMsgBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 客户端的心跳发送
 *与服务器端的空闲检测相配合，客户端需要定期发送数据包到服务器端，通常这个数
 * 据包称为心跳数据包。接下来，定义一个Handler业务处理器定期发送心跳数据包给服务器
 * 端。
 * 在HeartBeatClientHandler实例被加入到流水线时，它重写的handlerAdded方法被回调。
 * 在handlerAdded（…）方法中，开始调用heartBeat()方法，发送心跳数据包。heartBeat是一
 * 个不断递归调用的方法，它的递归调用的方式比较特别：使用了ctx.executor()获取当前通
 * 道绑定的Reactor反应器NIO线程，然后通过NIO线程的schedule()定时调度方法，隔一段时
 * 间（50s）执行一次回调，向服务器端发送一个心跳数据包，并递归设置下一次心跳发送任
 * 务。
 * 客户端的心跳发送间隔要比服务器端的空闲检测时间间隔要短，一般来说，要比服务
 * 器端监测间隔的一半要短一些，可以直接定义为空闲检测时间间隔的1/3。这样做的目的就
 * 是防止公网偶发的秒级抖动。
 */
@Slf4j
@ChannelHandler.Sharable
@Service("HeartBeatClientHandler")
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    //心跳的时间间隔，单位为s
    private static final int HEARTBEAT_INTERVAL = 100;

    //在Handler被加入到Pipeline时，开始发送心跳
    @Override
    public void handlerAdded(ChannelHandlerContext ctx)
            throws Exception {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgBuilder builder =
                new HeartBeatMsgBuilder(user, session);

        ProtoMsg.Message message = builder.buildMsg();
        //发送心跳
        heartBeat(ctx, message);
    }

    //使用定时器，发送心跳报文
    public void heartBeat(ChannelHandlerContext ctx,
                          ProtoMsg.Message heartbeatMsg) {
        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                log.info(" 发送 HEART_BEAT  消息 to server");
                ctx.writeAndFlush(heartbeatMsg);

                //递归调用，发送下一次的心跳
                heartBeat(ctx, heartbeatMsg);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 接受到服务器的心跳回写
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
        if (headType.equals(ProtoMsg.HeadType.HEART_BEAT)) {

            log.info(" 收到回写的 HEART_BEAT  消息 from server");

            return;
        } else {
            super.channelRead(ctx, msg);

        }

    }

}

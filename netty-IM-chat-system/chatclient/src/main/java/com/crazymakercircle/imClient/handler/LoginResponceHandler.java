package com.crazymakercircle.imClient.handler;


import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * LoginResponceHandler登录响应处理器对消息类型进行判断：
 * （1）如果消息类型是请求响应消息并且登录成功，则取出绑定的会话（Session），
 * 再设置登录成功的状态。完成登录成功处理之后，进行其他的客户端业务处理。
 * （2）如果消息类型不是请求响应消息，则调用父类默认的super.channelRead()入站处
 * 理方法，将数据包交给流水线的下一站Handler业务处理器去处理。
 */
@Slf4j
@ChannelHandler.Sharable
@Service("LoginResponceHandler")
public class LoginResponceHandler extends ChannelInboundHandlerAdapter {
    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message) msg).getType();
        if (!headType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE)) {
            super.channelRead(ctx, msg);
            return;
        }


        //判断返回是否成功
        ProtoMsg.LoginResponse info = pkg.getLoginResponse();

        ProtoInstant.ResultCodeEnum result =
                ProtoInstant.ResultCodeEnum.values()[info.getCode()];

        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS)) {
            //登录失败
            log.info(result.getDesc());
        } else {
            //登录成功
            ClientSession.loginSuccess(ctx, pkg);
            ChannelPipeline p = ctx.pipeline();
            //移除登录响应处理器
            p.remove(this);

            //在编码器后面，动态插入心跳处理器
            p.addAfter("encoder", "heartbeat", new HeartBeatClientHandler());
        }

    }

}

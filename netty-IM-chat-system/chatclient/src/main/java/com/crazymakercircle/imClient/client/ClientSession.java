package com.crazymakercircle.imClient.client;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现客户端 Session会话
 * 首先，ClientSession是一个很重要的胶水类，有两个成员：一个是user，代表用户，另
 * 一个是channel，代表了连接的通道。在实际开发中，这两个成员的作用是：
 * （1）通过user，ClientSession可以获得当前的用户信息；
 * （2）通过channel，ClientSession可以向服务器端发送消息。
 * ClientSession会话“左拥右抱”，左手“拥有”用户消息，右手“抱有”服务器端的连
 * 接，其通过user成员可以获取当前的用户信息，其借助channel通道可以写入Protobuf数据包
 * 到对端，或者关闭Netty连接。
 * 其次，客户端会话ClientSession保存着当前的状态：
 * （1）是否成功连接isConnected； （2）是否成功登录isLogin。
 * 第三：ClientSession绑定在Channel上，因而可以在入站处理时，可以通过Channel反向
 * 取得绑定的ClientSession，从而可以对应到user信息。这一点非常重要，在疯狂创客圈社群
 * 中，总是有人问，“如何将Channel与用户对应呢”，其答案就在于ClientSession与Channel
 * 的双向绑定关系上，通过Channel可以找到绑定的ClientSession，进一步找要对应的用户，
 * 从而实现Channel与用户对应关系。
 */
@Slf4j
@Data
public class ClientSession {


    public static final AttributeKey<ClientSession> SESSION_KEY =
            AttributeKey.valueOf("SESSION_KEY");


    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存登录后的服务端sessionid
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    /**
     * session中存储的session 变量属性值
     */
    private Map<String, Object> map = new HashMap<String, Object>();

    //绑定通道
    public ClientSession(Channel channel) {
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    //登录成功之后,设置sessionId
    public static void loginSuccess(
            ChannelHandlerContext ctx, ProtoMsg.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        log.info("登录成功");
    }

    //获取channel
    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    public String getRemoteAddress() {
        return channel.remoteAddress().toString();
    }

    //写protobuf 数据帧
    public ChannelFuture witeAndFlush(Object pkg) {
        ChannelFuture f = channel.writeAndFlush(pkg);
        return f;
    }

    public void writeAndClose(Object pkg) {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    //关闭通道
    public void close() {
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.error("连接顺利断开");
                }
            }
        });
    }


}

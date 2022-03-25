package com.example.netty.handle;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//这个注解的作用是标注一个Handler实例可以被多个通道安全地共享
//如果没有加@ChannelHandler.Sharable注解，试图将同一个Handler实例添加到
//多个ChannelPipeline通道流水线时，Netty将会抛出异常。
@ChannelHandler.Sharable
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        Logger.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));

        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        Logger.info("server received: " + new String(arr, "UTF-8"));

        //写回数据，异步任务
        Logger.info("写回前，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener((ChannelFuture futureListener) -> {
            Logger.info("写回后，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        });
    }
}

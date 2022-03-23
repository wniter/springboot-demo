package com.example.netty.basic;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 在反应器（Reactor）模式中，所有的业务处理都在Handler处理器中完成，业务处理一
 * 般需要自己编写，这里编写一个新类：NettyDiscardHandler。
 */
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    //业务代码
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf in = (ByteBuf) msg;
        try {
            Logger.info("收到消息,丢弃如下:");
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
            }
            System.out.println();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

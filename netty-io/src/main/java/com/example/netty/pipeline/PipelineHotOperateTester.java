package com.example.netty.pipeline;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 在流水线上热插拔Handler处理器
 *Netty中的处理器流水线是一个双向链表。在程序执行过程中，可以动态进行业务处理
 * 器的热插拔：动态地增加、删除流水线上的业务处理器Handler。
 *
 */
public class PipelineHotOperateTester {
    static class SimpleInHandlerA extends ChannelInboundHandlerAdapter {

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 A: 被回调 ");
            super.channelRead(ctx, msg);
            //从流水线删除当前Handler
            ctx.pipeline().remove(this);
        }

    }

    static class SimpleInHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 B: 被回调 ");
            super.channelRead(ctx, msg);
        }
    }

    static class SimpleInHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 C: 被回调 ");
            super.channelRead(ctx, msg);
        }
    }

    //测试处理器的热拔插
    @Test
    public void testPipelineHotOperating() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerB());
                ch.pipeline().addLast(new SimpleInHandlerC());

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //第一次向通道写入站报文
        channel.writeInbound(buf);

        //第二次向通道写入站报文
        channel.writeInbound(buf);
        //第三次向通道写入站报文
        channel.writeInbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * [main|PipelineHotOperateTester$SimpleInHandlerA.channelRead] |>  入站处理器 A: 被回调
     * [main|PipelineHotOperateTester$SimpleInHandlerB.channelRead] |>  入站处理器 B: 被回调
     * [main|PipelineHotOperateTester$SimpleInHandlerC.channelRead] |>  入站处理器 C: 被回调
     * [main|PipelineHotOperateTester$SimpleInHandlerB.channelRead] |>  入站处理器 B: 被回调
     * [main|PipelineHotOperateTester$SimpleInHandlerC.channelRead] |>  入站处理器 C: 被回调
     * [main|PipelineHotOperateTester$SimpleInHandlerB.channelRead] |>  入站处理器 B: 被回调
     * [main|PipelineHotOperateTester$SimpleInHandlerC.channelRead] |>  入站处理器 C: 被回调
     */
}

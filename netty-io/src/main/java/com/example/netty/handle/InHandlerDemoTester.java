package com.example.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 *首先对处理器的方法进行分类：（1）生命周期方法，（2）
 * 数据入站回调方法。上面的几个方法中，channelRead、channelReadComplete是入站处理方
 * 法；而其他的6个方法是入站处理器的周期方法。从输出的结果可以看到，ChannelHandler
 * 中的回调方法的执行顺序为：
 * handlerAdded()→ channelRegistered() →channelActive() →数据传输的入站回调
 * → channelInactive()→ channelUnregistered() →handlerRemoved()
 * 其中，数据传输的入站回调过程为：
 * channelRead() → channelReadComplete()
 * 读数据的入站回调过程，会根据入站数据的数量被重复调用，每一次有ByteBuf数据包
 * 入站都会调用到。
 * 除了两个入站回调方法外，其余的6个方法都和ChannelHandler的生命周期有关，具体
 * 的介绍如下：
 * （1）handlerAdded() ：当业务处理器被加入到流水线后，此方法将被回调。也就是在
 * 完成ch.pipeline().addLast(handler)语句之后，会回调handlerAdded()。 （2）channelRegistered()：当通道成功绑定一个NioEventLoop反应器后，此方法将被回
 * 调。
 * （3）channelActive()：当通道激活成功后，此方法将被回调。通道激活成功指的是，
 * 所有的业务处理器添加、注册的异步任务完成，并且与NioEventLoop反应器绑定的异步任
 * 务完成。
 * （4）channelInactive()：当通道的底层连接已经不是ESTABLISH状态，或者底层连接
 * 已经关闭时，会首先回调所有业务处理器的channelInactive()方法。
 * （5）channelUnregistered()：通道和NioEventLoop反应器解除绑定，移除掉对这条通道
 * 的事件处理之后，回调所有业务处理器的channelUnregistered ()方法。
 * （6）handlerRemoved()：最后，Netty会移除掉通道上所有的业务处理器，并且回调所
 * 有的业务处理器的handlerRemoved()方法。
 * 在上面的6个生命周期方法中，前面3个在通道创建和绑定的时候被先后回调，后面3个
 * 在通道关闭的时候会先后被回调。
 * 除了生命周期的回调，数据传输的入站回调方法。对于Inhandler入站处理器，有两个
 * 很重要的回调方法为：
 * （1）channelRead()：有数据包入站，通道可读。流水线会启动入站处理流程，从前向
 * 后，入站处理器的channelRead()方法会被依次回调到。
 * （2）channelReadComplete()：流水线完成入站处理后，会从前向后，依次回调每个入
 * 站处理器的channelReadComplete()方法，表示数据读取完毕。
 *
 */
public class InHandlerDemoTester {
    /**
     * [main|InHandlerDemo.handlerAdded] |>  被调用：handlerAdded()
     * [main|InHandlerDemo.channelRegistered] |>  被调用：channelRegistered()
     * [main|InHandlerDemo.channelActive] |>  被调用：channelActive()
     * [main|InHandlerDemo.channelRead] |>  被调用：channelRead()
     * [main|InHandlerDemo.channelReadComplete] |>  被调用：channelReadComplete()
     * [main|InHandlerDemo.channelRead] |>  被调用：channelRead()
     * [main|InHandlerDemo.channelReadComplete] |>  被调用：channelReadComplete()
     * [main|InHandlerDemo.channelInactive] |>  被调用：channelInactive()
     * [main|InHandlerDemo.channelUnregistered] |>  被调用: channelUnregistered()
     * [main|InHandlerDemo.handlerRemoved] |>  被调用：handlerRemoved()
     */
    @Test
    public void testInHandlerLifeCircle() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        //初始化处理器
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);
            }
        };
        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //模拟入站，写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        //模拟入站，再写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        //通道关闭
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

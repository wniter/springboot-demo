package com.example.netty.decoder;


import com.example.common.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * /**
 * 解码器
 * （1）固定长度数据包解码器——FixedLengthFrameDecoder
 * 适用场景：每个接收到的数据包的长度，都是固定的，例如 100个字节。在这种场景
 * 下，只需要把这个解码器加到流水线中，它会把入站ByteBuf数据包拆分成一个个长度为
 * 100的数据包，然后发往下一个channelHandler入站处理器。
 * （2）行分割数据包解码器——LineBasedFrameDecoder
 * 适用场景：每个ByteBuf数据包，使用换行符（或者回车换行符）作为数据包的边界分
 * 割符。在这种场景下，只需要把这个LineBasedFrameDecoder解码器加到流水线中，Netty会
 * 使用换行分隔符，把ByteBuf数据包分割成一个一个完整的应用层ByteBuf数据包，再发送
 * 到下一站。
 * （3）自定义分隔符数据包解码器——DelimiterBasedFrameDecoder
 * DelimiterBasedFrameDecoder是LineBasedFrameDecoder按照行分割的通用版本。不同之
 * 处在于，这个解码器更加灵活，可以自定义分隔符，而不是局限于换行符。如果使用这个
 * 解码器，那么所接收到的数据包，末尾必须带上对应的分隔符。
 * （4）自定义长度数据包解码器——LengthFieldBasedFrameDecoder
 * 这是一种基于灵活长度的解码器。在ByteBuf数据包中，加了一个长度域字段，保存了
 * 原始数据包的长度。解码的时候，会按照这个长度进行原始数据包的提取。此解码器在所
 * 有开箱即用解码器中是最为复杂的一种，后面会重点介绍。
 */


public class NettyOpenBoxDecoder {
    public static final int MAGICCODE = 9999;
    public static final int VERSION = 100;
    static String spliter = "\r\n";
    static String spliter2 = "\t";
    static String content = "疯狂创客圈：高性能学习社群!";

    /**
     * LineBasedFrameDecoder 使用实例
     */
    @Test
    public void testLineBasedFrameDecoder() {
        try {
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 0; j < 100; j++) {

                //1-3之间的随机数
                int random = RandomUtil.randInMod(3);
                ByteBuf buf = Unpooled.buffer();
                for (int k = 0; k < random; k++) {
                    buf.writeBytes(content.getBytes("UTF-8"));
                }
                buf.writeBytes(spliter.getBytes("UTF-8"));
                channel.writeInbound(buf);
            }


            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * LineBasedFrameDecoder 使用实例
     */
    @Test
    public void testLengthFieldBasedFrameDecoder() {
        try {

            final LengthFieldBasedFrameDecoder spliter =
                    new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 0; j < 100; j++) {
                //1-3之间的随机数
                int random = RandomUtil.randInMod(3);
                ByteBuf buf = Unpooled.buffer();
                byte[] bytes = content.getBytes("UTF-8");
                buf.writeInt(bytes.length * random);
                for (int k = 0; k < random; k++) {
                    buf.writeBytes(bytes);
                }
                channel.writeInbound(buf);
            }


            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * LineBasedFrameDecoder 使用实例
     */
    @Test
    public void testDelimiterBasedFrameDecoder() {
        try {
            final ByteBuf delimiter = Unpooled.copiedBuffer(spliter2.getBytes("UTF-8"));
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(
                            new DelimiterBasedFrameDecoder(1024, true, delimiter));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);
            for (int j = 0; j < 100; j++) {

                //1-3之间的随机数
                int random = RandomUtil.randInMod(3);
                ByteBuf buf = Unpooled.buffer();
                for (int k = 0; k < random; k++) {
                    buf.writeBytes(content.getBytes("UTF-8"));
                }
                buf.writeBytes(spliter2.getBytes("UTF-8"));
                channel.writeInbound(buf);
            }


            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * LengthFieldBasedFrameDecoder 使用实例
     */
    @Test
    public void testLengthFieldBasedFrameDecoder1() {
        try {

            final LengthFieldBasedFrameDecoder spliter =
                    new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 1; j <= 100; j++) {
                ByteBuf buf = Unpooled.buffer();
                String s = j + "次发送->" + content;
                byte[] bytes = s.getBytes("UTF-8");
                buf.writeInt(bytes.length);
                System.out.println("bytes length = " + bytes.length);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * LengthFieldBasedFrameDecoder 使用实例
     */
    @Test
    public void testLengthFieldBasedFrameDecoder2() {
        try {

            final LengthFieldBasedFrameDecoder spliter =
                    new LengthFieldBasedFrameDecoder(1024, 0, 4, 2, 6);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 1; j <= 100; j++) {
                ByteBuf buf = Unpooled.buffer();
                String s = j + "次发送->" + content;
                byte[] bytes = s.getBytes("UTF-8");
                buf.writeInt(bytes.length);
                buf.writeChar(VERSION);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * LengthFieldBasedFrameDecoder 使用实例 3
     */
    @Test
    public void testLengthFieldBasedFrameDecoder3() {
        try {

            final LengthFieldBasedFrameDecoder spliter =
                    new LengthFieldBasedFrameDecoder(1024, 2, 4, 4, 10);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 1; j <= 100; j++) {
                ByteBuf buf = Unpooled.buffer();
                String s = j + "次发送->" + content;
                byte[] bytes = s.getBytes("UTF-8");
                buf.writeChar(VERSION);
                buf.writeInt(bytes.length);
                buf.writeInt(MAGICCODE);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

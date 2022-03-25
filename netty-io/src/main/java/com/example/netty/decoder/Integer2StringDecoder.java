package com.example.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * MessageToMessageDecoder解码器
 * 需要继承一个新的Netty解码
 * 器基类：MessageToMessageDecoder<I>。在继承它的时候，需要明确的泛型实参<I>，其作
 * 用就是指定入站消息的Java POJO类型
 *
 */
public class Integer2StringDecoder extends
        MessageToMessageDecoder<Integer> {
    @Override
    public void decode(ChannelHandlerContext ctx, Integer msg,
                       List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}

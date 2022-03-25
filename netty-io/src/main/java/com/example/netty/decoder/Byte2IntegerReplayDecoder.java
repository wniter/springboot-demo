package com.example.netty.decoder;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * ReplayingDecoder解码器
 * ReplayingDecoder类是ByteToMessageDecoder的子类。其作用是：
 * ⚫ 在读取ByteBuf缓冲区的数据之前，需要检查缓冲区是否有足够的字节。
 * ⚫ 若ByteBuf中有足够的字节，则会正常读取；反之，如果没有足够的字节，则会停止解码
 *
 * 继承ReplayingDecoder实现一个解码器，就不用编
 * 写长度判断的代码。ReplayingDecoder进行长度判断的原理，其实很简单：它的内部定义了
 * 一个新的二进制缓冲区类，对ByteBuf缓冲区进行了装饰，这个类名为
 * ReplayingDecoderBuffer。该装饰器的特点是：在缓冲区真正读数据之前，首先进行长度的
 * 判断：如果长度合格，则读取数据；否则，抛出ReplayError。ReplayingDecoder捕获到
 * ReplayError后，会留着数据，等待下一次IO事件到来时再读取。
 */
public class Byte2IntegerReplayDecoder extends ReplayingDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) {
        int i = in.readInt();
        Logger.info("解码出一个整数: " + i);
        out.add(i);
    }
}

package com.example.netty.decoder;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义Byte2IntegerDecoder整数解码器
 * 其功能是：将ByteBuf缓冲区中的字节，解码成Integer整数类型。
 *（1）定义一个新的整数解码器——Byte2IntegerDecoder类，让这个类继承Netty的字节
 * 码解码抽象类ByteToMessageDecoder。
 * （2）实现父类的decode方法，将ByteBuf缓冲区数据，解码成以一个一个的Integer对
 * 象。
 * （3）在decode方法中，将解码后得到的Integer整数加入到父类入的List<Object>实参
 * 中。
 * 首先，Byte2IntegerDecoder解码器继承自ByteToMessageDecode，实现其decode方法；
 * 其次，在decode方法中，通过ByteBuf的readInt( )实例方法，从输入缓冲区读取到整
 * 数，其作用是将二进制数据解码成一个一个的整数；
 * 再次，将解码后的整数增加decode方法的List<Object>列表参数中；最后，decode不断
 * 地循环解码，并且不断地添加到List<Object>结果容器中。
 * 前面反复讲到，decode方法处理完成后，基类会继续后面的传递处理：将List<Object>
 * 结果列表中所得到的整数，一个一个地传递到下一个Inbound入站处理器。
 */
public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) {
        while (in.readableBytes() >= 4) {
            int i = in.readInt();
            Logger.info("解码出一个整数: " + i);
            out.add(i);
        }
    }
}

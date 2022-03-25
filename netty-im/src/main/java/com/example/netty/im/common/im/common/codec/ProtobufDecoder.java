package com.example.netty.im.common.im.common.codec;


import com.example.netty.im.common.im.common.ProtoInstant;
import com.example.netty.im.common.im.common.bean.msg.ProtoMsg;
import com.example.netty.im.common.im.common.exception.InvalidFrameException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义Protobuf解码器
 * 自定义Protobuf解码器，通过继承Netty中基础的ByteToMessageDecoder解码器类实现，
 * 在其继承的decode方法中，将ByteBuf字节码解码成Protobuf的POJO实例，大致的过程如
 * 下：
 * （1）首先读取长度，如果长度位数不够，则终止读取。
 * （2）然后读取魔数、版本号等其他的字段。
 * （3）最后按照净长度读取内容。如果内容的字节数不够，则恢复到之前的起始位置
 * （也就是长度的位置），然后终止读取。
 */
@Slf4j
public class ProtobufDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        // 标记一下当前的readIndex的位置
        in.markReaderIndex();
        // 判断包头长度
        if (in.readableBytes() < 8) {// 不够包头
            return;
        }
        //读取魔数
        short magic = in.readShort();
        if (magic != ProtoInstant.MAGIC_CODE) {
            String error = "客户端口令不对:" + ctx.channel().remoteAddress();
            throw new InvalidFrameException(error);
        }
        //读取版本
        short version = in.readShort();
        // 读取传送过来的消息的长度。
        int length = in.readInt();

        // 长度如果小于0
        if (length < 0) {// 非法数据，关闭连接
            ctx.close();
        }

        if (length > in.readableBytes()) {// 读到的消息体长度如果小于传送过来的消息长度
            // 重置读取位置
            in.resetReaderIndex();
            return;
        }


        byte[] array;
        if (in.hasArray()) {
            //堆缓冲
            ByteBuf slice = in.slice();
            array = slice.array();
        } else {
            //直接缓冲
            array = new byte[length];
            in.readBytes(array, 0, length);
        }

//        if(in.refCnt()>0)
//        {
////            log.debug("释放临时缓冲");
//            in.release();
//        }

        // 字节转成对象
        ProtoMsg.Message outmsg =
                ProtoMsg.Message.parseFrom(array);


        if (outmsg != null) {
            // 获取业务消息
            out.add(outmsg);
        }

    }
}

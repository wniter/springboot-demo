package com.crazymakercircle.im.common.codec;

import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.im.common.exception.InvalidFrameException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义Protobuf编码器
 * 自定义Protobuf编码器，通过继承Netty中基础的MessageToByteEncoder编码器类，实现
 * 其抽象的编码方法encode(…)，在该方法中把以下内容写入到目标ByteBuf：
 * （1）写入待发送的Protobuf POJO实例的二进制字节长度；
 * （2）写入其他的字段，如魔数、版本号；
 * （3）写入Protobuf POJO实例的二进制字节码内容。
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

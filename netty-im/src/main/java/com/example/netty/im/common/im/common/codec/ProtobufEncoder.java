package com.example.netty.im.common.im.common.codec;


import com.example.netty.im.common.im.common.ProtoInstant;
import com.example.netty.im.common.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义ProtoBuf编解码器
 * 自定义Protobuf编码器，通过继承Netty中基础的MessageToByteEncoder编码器类，实现
 * 其抽象的编码方法encode(…)，在该方法中把以下内容写入到目标ByteBuf： （1）写入待发送的Protobuf POJO实例的二进制字节长度；
 * （2）写入其他的字段，如魔数、版本号；
 * （3）写入Protobuf POJO实例的二进制字节码内容。
 */

@Slf4j
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ProtoMsg.Message msg, ByteBuf out)
            throws Exception {
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION_CODE);

        byte[] bytes = msg.toByteArray();// 将对象转换为byte

        // 加密消息体
        /*ThreeDES des = channel.channel().attr(Constants.ENCRYPT).get();
        byte[] encryptByte = des.encrypt(bytes);*/
        int length = bytes.length;// 读取消息的长度


        // 先将消息长度写入，也就是消息头
        out.writeInt(length);
        // 消息体中包含我们要发送的数据
        out.writeBytes(msg.toByteArray());

/*        log.debug("send "
                + "[remote ip:" + ctx.channel().remoteAddress()
                + "][total length:" + length
                + "][bare length:" + msg.getSerializedSize() + "]");*/


    }

}

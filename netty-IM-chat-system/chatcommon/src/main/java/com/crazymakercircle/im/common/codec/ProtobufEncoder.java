package com.crazymakercircle.im.common.codec;


import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义Protobuf解码器
 * 自定义Protobuf解码器，通过继承Netty中基础的ByteToMessageDecoder解码器类实现，
 * 在其继承的decode方法中，将ByteBuf字节码解码成Protobuf的POJO实例，大致的过程如下：
 * （1）首先读取长度，如果长度位数不够，则终止读取。
 * （2）然后读取魔数、版本号等其他的字段。
 * （3）最后按照净长度读取内容。如果内容的字节数不够，则恢复到之前的起始位置
 * （也就是长度的位置），然后终止读取.
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

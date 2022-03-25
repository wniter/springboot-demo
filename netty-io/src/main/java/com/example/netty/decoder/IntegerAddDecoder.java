package com.example.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * IntegerAddDecoder类继承了ReplayingDecoder<IntegerAddDecoder.PHASE>，其后面的
 * 泛型实参为IntegerAddDecoder.PHASE自定义的状态类型，是一个enum枚举类型，用来作为
 * 泛型变量“state”的实际类型，该枚举值的有两个常量：
 * （1）PHASE_1：表示第一个阶段，在此阶段将读取第一个整数。
 * （2）PHASE_2：表示第二个阶段，在此阶段将读取后面的第二个整数，然后相加。
 * 父类的成员变量state的值，可能为PHASE_1或者PHASE_2，代表当前的阶段。state值
 * 需要在构造函数中进行初始化，在这里的子类构造函数中，调用了super(Status.PARSE_1)将
 * state初始化为第一个阶段。
 * 在IntegerAddDecoder类中，每一次decode方法中的解码，有两个阶段：
 * （1）第一个阶段，解码出前一个整数。
 * （2）第二个阶段，解码出后一个整数，然后求和。
 * 每一个阶段一完成，就通过checkpoint（PHASE）方法，把当前的state状态设置为新的
 * PHASE枚举值，checkpoint（PHASE）类似于state属性的setter方法。checkpoint（…）方法
 * 有两个作用：
 * （1）设置state属性的值，更新一下当前的状态。
 * （2）还有一个非常大的作用，就是设置“读指针检查点”。
 * 什么是ReplayingDecoder的“读指针”呢？就是ReplayingDecoder提取二进制数据的
 * ByteBuf缓冲区的readerIndex读指针。“读指针检查点”是ReplayingDecoder类的另一个重
 * 要的成员，它用于暂存内部ReplayingDecoderBuffer装饰器缓冲区的readerIndex读指针，有
 * 点儿类似于mark标记。当读数据时，一旦缓冲区可读的二进制数据不够，缓冲区装饰器
 * ReplayingDecoderBuffer在抛出ReplayError异常之前，会把readerIndex读指针的值，还原到
 * 之前通过checkpoint（…）方法设置的“读指针检查点”。于是乎，在ReplayingDecoder下
 * 一次重新读取时，还会从“读指针检查点”的位置开始读取。
 */
public class IntegerAddDecoder
        extends ReplayingDecoder<IntegerAddDecoder.Status> {

    enum Status {
        PARSE_1, PARSE_2
    }

    private int first;
    private int second;

    public IntegerAddDecoder() {
        //构造函数中，需要初始化父类的state 属性，表示当前阶段
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {


        switch (state()) {
            case PARSE_1:
                //从装饰器ByteBuf 中读取数据
                first = in.readInt();
                //第一步解析成功，
                // 进入第二步，并且设置“读指针断点”为当前的读取位置
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                second = in.readInt();
                Integer sum = first + second;
                out.add(sum);
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}
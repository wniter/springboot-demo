package com.example.netty.bytebuffer;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * slice切片浅层复制
 * （1）public ByteBuf slice()
 * （2）public ByteBuf slice(int index, int length)
 * 调用slice()方法后，返回的切片是一个新的ByteBuf对象，该对象的几个重要属性值，
 * 大致如下：
 * ⚫ readerIndex（读指针）值为 0。 ⚫ writerIndex（写指针）值为源ByteBuf的readableBytes()可读字节数。
 * ⚫ maxCapacity（最大容量）值为源ByteBuf的readableBytes( )可读字节数。
 * 切片后的新ByteBuf有两个特点：
 * ⚫ 切片不可以写入，原因是：maxCapacity与writerIndex值相同。
 * ⚫ 切片和源ByteBuf的可读字节数相同，原因是：切片后的可读字节数为自己的属性
 * writerIndex – readerIndex，也就是源ByteBuf的readableBytes() - 0。
 * 切片后的新ByteBuf和源ByteBuf的关联性：
 * ⚫ 切片不会复制源ByteBuf的底层数据，底层数组和源ByteBuf的底层数组是同一
 * 个。
 * ⚫ 切片不会改变源ByteBuf的引用计数
 *
 * duplicate整体浅层复制
 * 和slice切片不同，duplicate() 返回的是源ByteBuf的整个对象的一个浅层复制，包括如
 * 下内容：
 * ⚫ duplicate的读写指针、最大容量值，与源ByteBuf的读写指针相同。
 * ⚫ duplicate() 不会改变源ByteBuf的引用计数。
 * ⚫ duplicate() 不会复制源ByteBuf的底层数据。
 * duplicate() 和slice() 方法都是浅层复制。不同的是，slice()方法是切取一段的浅层复
 * 制，而duplicate( )是整体的浅层复制
 */
public class SliceTest {
    @Test
    public  void testSlice() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("动作：分配 ByteBuf(9, 100)", buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("动作：写入4个字节 (1,2,3,4)", buffer);
        ByteBuf slice = buffer.slice();
        print("动作：切片 slice", slice);
    }

    /**
     * [main|SliceTest.print] |>  after ===========动作：分配 ByteBuf(9, 100)============
     * [main|SliceTest.print] |>  1.0 isReadable(): false
     * [main|SliceTest.print] |>  1.1 readerIndex(): 0
     * [main|SliceTest.print] |>  1.2 readableBytes(): 0
     * [main|SliceTest.print] |>  2.0 isWritable(): true
     * [main|SliceTest.print] |>  2.1 writerIndex(): 0
     * [main|SliceTest.print] |>  2.2 writableBytes(): 9
     * [main|SliceTest.print] |>  3.0 capacity(): 9
     * [main|SliceTest.print] |>  3.1 maxCapacity(): 100
     * [main|SliceTest.print] |>  3.2 maxWritableBytes(): 100
     * [main|SliceTest.print] |>  after ===========动作：写入4个字节 (1,2,3,4)============
     * [main|SliceTest.print] |>  1.0 isReadable(): true
     * [main|SliceTest.print] |>  1.1 readerIndex(): 0
     * [main|SliceTest.print] |>  1.2 readableBytes(): 4
     * [main|SliceTest.print] |>  2.0 isWritable(): true
     * [main|SliceTest.print] |>  2.1 writerIndex(): 4
     * [main|SliceTest.print] |>  2.2 writableBytes(): 5
     * [main|SliceTest.print] |>  3.0 capacity(): 9
     * [main|SliceTest.print] |>  3.1 maxCapacity(): 100
     * [main|SliceTest.print] |>  3.2 maxWritableBytes(): 96
     * [main|SliceTest.print] |>  after ===========动作：切片 slice============
     * [main|SliceTest.print] |>  1.0 isReadable(): true
     * [main|SliceTest.print] |>  1.1 readerIndex(): 0
     * [main|SliceTest.print] |>  1.2 readableBytes(): 4
     * [main|SliceTest.print] |>  2.0 isWritable(): false
     * [main|SliceTest.print] |>  2.1 writerIndex(): 4
     * [main|SliceTest.print] |>  2.2 writableBytes(): 0
     * [main|SliceTest.print] |>  3.0 capacity(): 4
     * [main|SliceTest.print] |>  3.1 maxCapacity(): 4
     * [main|SliceTest.print] |>  3.2 maxWritableBytes(): 0
     *
     * Process finished with exit code 0
     */


    public  void print(String action, ByteBuf b) {
        Logger.info("after ===========" + action + "============");
        Logger.info("1.0 isReadable(): " + b.isReadable());
        Logger.info("1.1 readerIndex(): " + b.readerIndex());
        Logger.info("1.2 readableBytes(): " + b.readableBytes());
        Logger.info("2.0 isWritable(): " + b.isWritable());
        Logger.info("2.1 writerIndex(): " + b.writerIndex());
        Logger.info("2.2 writableBytes(): " + b.writableBytes());
        Logger.info("3.0 capacity(): " + b.capacity());
        Logger.info("3.1 maxCapacity(): " + b.maxCapacity());
        Logger.info("3.2 maxWritableBytes(): " + b.maxWritableBytes());
    }

}
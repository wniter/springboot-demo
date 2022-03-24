package com.example.netty.bytebuffer;

import com.example.common.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import com.example.common.util.*;


/**
 * ByteBuf的基本使用分为三部分：
 * （1）分配一个ByteBuf实例；
 * （2）向ByteBuf写数据；
 * （3）从ByteBuf读数据。
 *
 */
public class WriteReadTest {

    @Test
    public void testWriteRead() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        print("动作：分配 ByteBuf(9, 100)", buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("动作：写入4个字节 (1,2,3,4)", buffer);
        Logger.info("start==========:get==========");
        getByteBuf(buffer);
        print("动作：取数据 ByteBuf", buffer);
        Logger.info("start==========:read==========");
        readByteBuf(buffer);
        print("动作：读完 ByteBuf", buffer);
    }

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


    //读取一个字节
    private void readByteBuf(ByteBuf buffer) {
        while (buffer.isReadable()) {
            Logger.info("读取一个字节:" + buffer.readByte());
        }
    }


    //读取一个字节，不改变指针
    private void getByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.readableBytes(); i++) {
            Logger.info("读取一个字节:" + buffer.getByte(i));
        }
    }
/**
 * [main|WriteReadTest.print] |>  after ===========动作：分配 ByteBuf(9, 100)============
 * [main|WriteReadTest.print] |>  1.0 isReadable(): false
 * [main|WriteReadTest.print] |>  1.1 readerIndex(): 0
 * [main|WriteReadTest.print] |>  1.2 readableBytes(): 0
 * [main|WriteReadTest.print] |>  2.0 isWritable(): true
 * [main|WriteReadTest.print] |>  2.1 writerIndex(): 0
 * [main|WriteReadTest.print] |>  2.2 writableBytes(): 256
 * [main|WriteReadTest.print] |>  3.0 capacity(): 256
 * [main|WriteReadTest.print] |>  3.1 maxCapacity(): 2147483647
 * [main|WriteReadTest.print] |>  3.2 maxWritableBytes(): 2147483647
 * [main|WriteReadTest.print] |>  after ===========动作：写入4个字节 (1,2,3,4)============
 * [main|WriteReadTest.print] |>  1.0 isReadable(): true
 * [main|WriteReadTest.print] |>  1.1 readerIndex(): 0
 * [main|WriteReadTest.print] |>  1.2 readableBytes(): 4
 * [main|WriteReadTest.print] |>  2.0 isWritable(): true
 * [main|WriteReadTest.print] |>  2.1 writerIndex(): 4
 * [main|WriteReadTest.print] |>  2.2 writableBytes(): 252
 * [main|WriteReadTest.print] |>  3.0 capacity(): 256
 * [main|WriteReadTest.print] |>  3.1 maxCapacity(): 2147483647
 * [main|WriteReadTest.print] |>  3.2 maxWritableBytes(): 2147483643
 * [main|WriteReadTest.testWriteRead] |>  start==========:get==========
 * [main|WriteReadTest.getByteBuf] |>  读取一个字节:1
 * [main|WriteReadTest.getByteBuf] |>  读取一个字节:2
 * [main|WriteReadTest.getByteBuf] |>  读取一个字节:3
 * [main|WriteReadTest.getByteBuf] |>  读取一个字节:4
 * [main|WriteReadTest.print] |>  after ===========动作：取数据 ByteBuf============
 * [main|WriteReadTest.print] |>  1.0 isReadable(): true
 * [main|WriteReadTest.print] |>  1.1 readerIndex(): 0
 * [main|WriteReadTest.print] |>  1.2 readableBytes(): 4
 * [main|WriteReadTest.print] |>  2.0 isWritable(): true
 * [main|WriteReadTest.print] |>  2.1 writerIndex(): 4
 * [main|WriteReadTest.print] |>  2.2 writableBytes(): 252
 * [main|WriteReadTest.print] |>  3.0 capacity(): 256
 * [main|WriteReadTest.print] |>  3.1 maxCapacity(): 2147483647
 * [main|WriteReadTest.print] |>  3.2 maxWritableBytes(): 2147483643
 * [main|WriteReadTest.testWriteRead] |>  start==========:read==========
 * [main|WriteReadTest.readByteBuf] |>  读取一个字节:1
 * [main|WriteReadTest.readByteBuf] |>  读取一个字节:2
 * [main|WriteReadTest.readByteBuf] |>  读取一个字节:3
 * [main|WriteReadTest.readByteBuf] |>  读取一个字节:4
 * [main|WriteReadTest.print] |>  after ===========动作：读完 ByteBuf============
 * [main|WriteReadTest.print] |>  1.0 isReadable(): false
 * [main|WriteReadTest.print] |>  1.1 readerIndex(): 4
 * [main|WriteReadTest.print] |>  1.2 readableBytes(): 0
 * [main|WriteReadTest.print] |>  2.0 isWritable(): true
 * [main|WriteReadTest.print] |>  2.1 writerIndex(): 4
 * [main|WriteReadTest.print] |>  2.2 writableBytes(): 252
 * [main|WriteReadTest.print] |>  3.0 capacity(): 256
 * [main|WriteReadTest.print] |>  3.1 maxCapacity(): 2147483647
 * [main|WriteReadTest.print] |>  3.2 maxWritableBytes(): 2147483643
 *
 * Process finished with exit code 0
 */
}

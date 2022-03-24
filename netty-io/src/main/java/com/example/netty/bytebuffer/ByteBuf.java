package com.example.netty.bytebuffer;
/**
 * ByteBuf是一个字节容器，内部是一个字节数组。
 * 第一个部分是已用字节，表示已经使用完的废弃的无效字节；第二部分是可读字节，
 * 这部分数据是ByteBuf保存的有效数据，从ByteBuf中读取的数据都来自这一部分；第三部
 * 分是可写字节，写入到ByteBuf的数据都会写到这一部分中；第四部分是可扩容字节，表示
 * 的是该ByteBuf最多还能扩容的大小。
 *
 * ByteBuf的三个重要属性
 *⚫ readerIndex（读指针）
 * ⚫ writerIndex（写指针）
 * ⚫ maxCapacity（最大容量）
 *
 *
 * 对ByteBuf的这三个重要属性，详细介绍如下：
 * ⚫ readerIndex（读指针）：指示读取的起始位置。每读取一个字节，readerIndex自动
 * 增加1。一旦 readerIndex与writerIndex相等，则表示ByteBuf不可读了。
 * ⚫ writerIndex（写指针）：指示写入的起始位置。每写一个字节，writerIndex自动增
 * 加1。一旦增加到writerIndex与capacity()容量相等，则表示ByteBuf已经不可写了。
 * 注意，capacity()是一个成员方法，不是一个成员属性，它表示ByteBuf中可以写入
 * 的容量，而且它的值不一定是最大容量maxCapacity值。
 * ⚫ maxCapacity（最大容量）：表示ByteBuf可以扩容的最大容量。当向ByteBuf写数
 * 据的时候，如果容量不足，可以进行扩容。扩容的最大限度由maxCapacity的值来
 * 设定，超过maxCapacity就会报错。
 *
 * ByteBuf的三组方法
 * 第一组：容量系列
 * ⚫ capacity()：表示ByteBuf的容量，它的值是以下三部分之和：废弃的字节数、可读
 * 字节数和可写字节数。
 * ⚫ maxCapacity()：表示ByteBuf最大能够容纳的最大字节数。当向ByteBuf中写数据
 * 的时候，如果发现容量不足，则进行扩容，直到扩容到maxCapacity设定的上限。
 * 第二组：写入系列
 * ⚫ isWritable() ：表示ByteBuf是否可写。如果capacity()容量大于writerIndex指针的位
 * 置，则表示可写，否则为不可写。注意：如果isWritable()返回false，并不代表不
 * 能再往ByteBuf中写数据了。如果Netty发现往ByteBuf中写数据写不进去的话，会
 * 自动扩容ByteBuf。 ⚫ writableBytes() ：取得可写入的字节数，它的值等于容量capacity()减去
 * writerIndex。 ⚫ maxWritableBytes() ：取得最大的可写字节数，它的值等于最大容量maxCapacity
 * 减去writerIndex。 ⚫ writeBytes(byte[] src) ：把入参src字节数组中的数据全部写到ByteBuf。这是最为
 * 常用的一个方法。
 * ⚫ writeTYPE(TYPE value）：写入基础数据类型的数据。TYPE表示基础数据类型，
 * 包含了 8大基础数据类型。具体如下：writeByte(…)、 writeBoolean(…)、
 * writeChar(…)、writeShort(…)、writeInt(…)、writeLong(…)、writeFloat(…)、
 * writeDouble(…)。 ⚫ setTYPE(TYPE value）：基础数据类型的设置，不改变writerIndex指针值，包含了
 * 8大基础数据类型的设置。具体如下：setByte(…)、 setBoolean(…)、setChar(…)、
 * setShort(…)、setInt(…)、setLong(…)、setFloat(…)、setDouble(…)。setType系列与
 * writeTYPE系列的不同：setType系列不改变写指针writerIndex的值；writeTYPE系
 * 列会改变写指针writerIndex的值。
 * ⚫ markWriterIndex()与resetWriterIndex()：这两个方法一起介绍。前一个方法表示把
 * 当前的写指针writerIndex属性的值保存在markedWriterIndex标记属性中；后一个方
 * 法表示把之前保存的markedWriterIndex的值恢复到写指针writerIndex属性中。这两
 * 个方法都用到了标记属性markedWriterIndex，相当于一个写指针的暂存属性。
 * 第三组：读取系列
 * ⚫ isReadable( ) ：返回ByteBuf是否可读。如果writerIndex指针的值大于readerIndex
 * 指针的值，则表示可读，否则为不可读。
 * ⚫ readableBytes( ) ：返回表示ByteBuf当前可读取的字节数，它的值等于writerIndex
 * 减去readerIndex。 ⚫ readBytes(byte[] dst)：将数据从ByteBuf读取到dst目标字节数组中，这里dst字节数
 * 组的大小，通常等于readableBytes()可读字节数。这个方法也是最为常用的一个方
 * 法之一。
 * ⚫ readType()：读取基础数据类型，可以读取 8大基础数据类型。具体如下：
 * readByte()、readBoolean()、readChar()、readShort()、readInt()、readLong()
 * readFloat()、readDouble() 。 ⚫ getTYPE()：读取基础数据类型，并且不改变readerIndex读指针的值。具体如下：
 * getByte()、 getBoolean()、getChar()、getShort()、getInt()、getLong()、getFloat()、
 * getDouble()。getType系列与readTYPE系列的不同：getType系列不会改变读指针
 * readerIndex的值；readTYPE系列会改变读指针readerIndex的值。
 * ⚫ markReaderIndex( )与resetReaderIndex( ) ：这两个方法一起介绍。前一个方法表示
 * 把当前的读指针readerIndex保存在markedReaderIndex属性中。后一个方法表示把
 * 保存在markedReaderIndex属性的值恢复到读指针readerIndex中。
 * markedReaderIndex属性定义在AbstractByteBuf抽象基类中，是一个标记属性，相
 * 当于一个读指针的暂存属性。
 */
//public class ByteBuf {
//}
/**
 ByteBuf的自动创建
 Netty的Reactor反应器线程会通过底层的Java NIO通
 道读数据，发生NIO读取的方法为AbstractNioByteChannel.NioByteUnsafe.read()方法
 public void read() {
 ....
 //channel 的 config 信息
 final ChannelConfig config = config();
 //获取通道的缓冲区分配器
 final ByteBufAllocator allocator = config.getAllocator();
 //channel 的 pipeline 流水线
 final ChannelPipeline pipeline = pipeline();
 //缓冲区分配时的大小推测与计算组件
 final RecvByteBufAllocator.Handle allocHandle =
 unsafe().recvBufAllocHandle();
 //输入缓冲变量
 ByteBuf byteBuf = null;
 Throwable exception = null;
 try {
 ....
 do {
 ....
 //使用缓冲区分配器、大小计算组件一起
 //由分配器按照计算好的大小分配的一个缓冲区
 byteBuf = allocHandle.allocate(allocator);
 ....
 //读取数据到缓冲区
 int localReadAmount = doReadBytes(byteBuf);
 ....
 //发送数据到流水线，进行入站处理
 pipeline.fireChannelRead(byteBuf);
 .....
 }while (++ messages < maxMessagesPerRead);
 .....
 } catch (Throwable t) {
 handleReadException(pipeline, byteBuf, t, close);
 }
 .....
 }

 Netty设计了一个RecvByteBufAllocator大小推测接口和一系列的大小推测实现类，帮助
 进行缓冲区大小的计算和推测。默认的缓冲区大小推测实现类为
 AdaptiveRecvByteBufAllocator，其特点是能够根据上一次接收数据的大小，来自动调整下
 一次缓冲区建立时分配的空间大小，从而帮助避免内存的浪费。

 自动释放方式一：TailContext自动释放
 自动释放方式二：SimpleChannelInboundHandler自动释放
 出站处理时的自动释放
 */
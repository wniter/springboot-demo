package com.example.netty.bufferDemo;

import com.example.netty.common.util.Logger;

import java.nio.IntBuffer;

/**
 * 详解NIO Buffer类的重要方法：
 * <p>
 * allocate()创建缓冲区
 * 在使用Buffer（缓冲区）实例之前，我们首先需要获取Buffer子类的实例对象，并且分
 * 配内存空间。如果需要获取一个Buffer实例对象，并不是使用子类的构造器来创建一个实
 * 例对象，而是调用子类的allocate()方法。
 * put()写入到缓冲区
 * 在调用allocate方法分配内存、返回了实例对象后，缓冲区实例对象处于写模式，可以
 * 写入对象，而如果要写入对象到缓冲区，需要调用put方法。put方法很简单，只有一个参
 * 数，即为所需要写入的对象。只不过，写入的数据类型要求与缓冲区的类型保持一致。
 * flip()翻转
 * 。flip()翻转方法是Buffer类提供的一个模式转变的重要方法，它的作用就是将写入模式翻转成读
 * 取模式
 * get()从缓冲区读取
 * 使用调用flip方法将缓冲区切换成读取模式之后，就可以开始从缓冲区中进行数据读取
 * 了。读取数据的方法很简单，可以调用get方法每次从position的位置读取一个数据，并且进
 * 行相应的缓冲区属性的调整。
 * 那么，在读完之后是否可以立即对缓冲区进行数据写入呢？答案是不能。现在还处于
 * 读取模式，我们必须调用Buffer.clear()或Buffer.compact()方法，即清空或者压缩缓冲区，将
 * 缓冲区切换成写入模式，让其重新可写。
 * 此外还有一个问题：缓冲区是不是可以重复读呢？答案是可以的，既可以通过倒带方
 * 法rewind()去完成，也可以通过mark( )和reset( )两个方法组合实现。
 * rewind()倒带
 * 已经读完的数据，如果需要再读一遍，可以调用rewind()方法。rewind()也叫倒带，就
 * 像播放磁带一样倒回去，再重新播放。
 * rewind ()方法，主要是调整了缓冲区的position属性与mark标记属性，具体的调整规则:
 * （1）position重置为0，所以可以重读缓冲区中的所有数据；
 * （2）limit保持不变，数据量还是一样的，仍然表示能从缓冲区中读取的元素数量；
 * （3）mark标记被清理，表示之前的临时位置不能再用了。
 * 从JDK中可以查阅到Buffer.rewind()方法的源代码，具体如下：
 * public final Buffer rewind() {
 * position = 0;//重置为 0，所以可以重读缓冲区中的所有数据
 * mark = -1; // mark 标记被清理，表示之前的临时位置不能再用了
 * return this;
 * }
 *mark( )和reset( )
 *mark( )和reset( )两个方法是成套使用的：Buffer.mark()方法将当前position的值保存起
 * 来，放在mark属性中，让mark属性记住这个临时位置；之后，可以调用Buffer.reset()方法将
 * mark的值恢复到position中
 *k()方法，把当前位置position的值保存到mark属性中，这时mark属性的值为2。
 * 然后，就可以调用reset( )方法，将mark属性的值恢复到position中，这样就可以从位置
 * 2（第三个元素）开始重复读取。
 *clear( )清空缓冲区
 */
public class UseBuffer {

    /**
     *总体来说，使用Java NIO Buffer类的基本步骤如下:
     * （1）使用创建子类实例对象的allocate( )方法，创建一个Buffer类的实例对象。
     * （2）调用put( )方法，将数据写入到缓冲区中。
     * （3）写入完成后，在开始读取数据前，调用Buffer.flip( )方法，将缓冲区转换为读模式。
     * （4）调用get( )方法，可以从缓冲区中读取数据。
     * （5）读取完成后，调用Buffer.clear( )方法或Buffer.compact()方法，将缓冲区转换为写
     * 入模式，可以继续写入。
     */
    public static void main(String[] args) {
        Logger.info("分配内存");
        allocateTest();
        /**
         * [main|UseBuffer.main] |>  分配内存
         *         allocateTest |>  -----------allocate-------
         *         allocateTest |>  capacity:20
         *         allocateTest |>  position:0
         *         allocateTest |>  limit:20
         * [main|UseBuffer.main] |>  写入
         */
        Logger.info("写入");
        putTest();
        /**
         * [main|UseBuffer.main] |>  写入
         *              putTest |>  -----------put-------
         *              putTest |>  capacity:20
         *              putTest |>  position:10
         *              putTest |>  limit:20 
         */
        Logger.info("翻转");
        flipTest();
        /**
         * [main|UseBuffer.main] |>  翻转
         * [main|UseBuffer.flipTest] |>  ------------ flip ------------------
         * [main|UseBuffer.flipTest] |>  position=0
         * [main|UseBuffer.flipTest] |>  limit=10
         * [main|UseBuffer.flipTest] |>  capacity=20 
         */
        Logger.debug("读取");
        getTest();
        /**
         * [main|UseBuffer.getTest] |>  ------------ flip ------------------
         * [main|UseBuffer.getTest] |>  position=1
         * [main|UseBuffer.getTest] |>  limit=10
         * [main|UseBuffer.getTest] |>  capacity=20
         */
        Logger.debug("重复读");
        rewindTest();
        reRead();
        /**
         * [main|UseBuffer.rewindTest] |>  ------------ flip ------------------
         * [main|UseBuffer.rewindTest] |>  position=2
         * [main|UseBuffer.rewindTest] |>  limit=10
         * [main|UseBuffer.rewindTest] |>  capacity=20
         *               reRead |>  j = 2
         *               reRead |>  j = 3
         *               reRead |>  j = 4
         *               reRead |>  j = 5
         *               reRead |>  j = 6
         *               reRead |>  ------------after reRead------------------
         *               reRead |>  position=7
         *               reRead |>  limit=10
         *               reRead |>  capacity=20
         */
        Logger.debug("make&reset写读");
        afterReset();
        /**
         *       afterReset |>  ------------after reset------------------
         *           afterReset |>  position=4
         *           afterReset |>  limit=10
         *           afterReset |>  capacity=20
         *           afterReset |>  j = 4
         *           afterReset |>  j = 5
         *           afterReset |>  j = 6
         */
        Logger.debug("清空");

        clearDemo();
        /**
         *        main |>  清空
         *            clearDemo |>  ------------after clear------------------
         *            clearDemo |>  position=0
         *            clearDemo |>  limit=20
         *            clearDemo |>  capacity=20
         */
    }



    /**
     * 调用flip方法后，新模式下可读上限limit的值，变成了之前写入模式下的position属性
     * 值，也就是5；而新的读取模式下的position值，简单粗暴地变成了0，表示从头开始读取。
     * 对flip()方法的从写入到读取转换的规则，再一次详细的介绍如下：
     * （1）首先，设置可读上限limit的属性值。将写入模式下的缓冲区中内容的最后写入位
     * 置position值，作为读取模式下的limit上限值。
     * （2）其次，把读的起始位置position的值设为0，表示从头开始读。
     * （3）最后，清除之前的mark标记，因为mark保存的是写入模式下的临时位置，发生
     * 模式翻转后，如果继续使用旧的mark标记，会造成位置混乱。
     * 有关上面的三步，其实可以查看Buffer.flip()方法的源代码，具体代码如下：
     * 当然，新的问题来了：在读取完成后，如何再一次将缓冲区切换成写入模式呢？答案
     * 是：可以调用Buffer.clear() 清空或者Buffer.compact()压缩方法，它们可以将缓冲区转换为
     * 写模式。总体的Buffer模式转换
     */
    private static void flipTest() {


        intBuffer.flip();
//输出缓冲区的主要属性值
        Logger.info("------------ flip ------------------");
        Logger.info("position=" + intBuffer.position());
        Logger.info("limit=" + intBuffer.limit());
        Logger.info("capacity=" + intBuffer.capacity());

    }

    private static void putTest() {
        for (int i = 0; i < 10; i++) {
            intBuffer.put(i);
        }
        Logger.debug("-----------put-------");
        Logger.debug("capacity:" + intBuffer.capacity());
        Logger.debug("position:" + intBuffer.position());
        Logger.debug("limit:" + intBuffer.limit());
    }

    //创建一个int的Buffer
    static IntBuffer intBuffer = null;

    public static void allocateTest() {
        intBuffer = IntBuffer.allocate(20);
        Logger.debug("-----------allocate-------");
        Logger.debug("capacity:" + intBuffer.capacity());
        Logger.debug("position:" + intBuffer.position());
        Logger.debug("limit:" + intBuffer.limit());
    }
    private static void clearDemo() {
        Logger.debug("------------after clear------------------");
        intBuffer.clear();
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
    }

    private static void afterReset() {
        Logger.debug("------------after reset------------------");
        intBuffer.reset();
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
        for (int i =2; i < 5; i++) {
            int j = intBuffer.get();
            Logger.debug("j = " + j);

        }
    }
    /**
     * rewind之后，重复读
     * 并且演示 mark 标记方法
     */
    private static void reRead() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                intBuffer.mark();
            }
            int j = intBuffer.get();
            Logger.debug("j = " + j);

        }
        Logger.debug("------------after reRead------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());

    }

    private static void rewindTest() {

        intBuffer.get();
//输出缓冲区的主要属性值
        Logger.info("------------ flip ------------------");
        Logger.info("position=" + intBuffer.position());
        Logger.info("limit=" + intBuffer.limit());
        Logger.info("capacity=" + intBuffer.capacity());
    }

    private static void getTest() {

        intBuffer.get();
//输出缓冲区的主要属性值
        Logger.info("------------ get ------------------");
        Logger.info("position=" + intBuffer.position());
        Logger.info("limit=" + intBuffer.limit());
        Logger.info("capacity=" + intBuffer.capacity());
    }
}

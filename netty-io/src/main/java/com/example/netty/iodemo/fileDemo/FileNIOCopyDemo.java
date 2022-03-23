package com.example.netty.iodemo.fileDemo;

import com.example.netty.NioDemoConfig;
import com.example.netty.common.util.IOUtil;
import com.example.netty.common.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * Channel（通道）的主要类型:FileChannel、SocketChannel、ServerSocketChannel、DatagramChannel。
 */

/**
 *  FileChannel文件通道 :FileChannel为阻塞模式，不能设置为非阻塞模式。
 *  下面分别介绍：FileChannel的获取、读取、写入、关闭四个操作。
 * 1. 获取FileChannel通道
 * 可以通过文件的输入流、输出流获取FileChannel文件通道，示例如下：
 * //创建一个文件输入流
 * FileInputStream fis = new FileInputStream(srcFile);
 * //获取文件流的通道
 * FileChannel inChannel = fis.getChannel();
 * //创建一个文件输出流
 * FileOutputStream fos = new FileOutputStream(destFile);
 * //获取文件流的通道
 * FileChannel outchannel = fos.getChannel();
 * 也可以通过RandomAccessFile文件随机访问类，获取FileChannel文件通道实例，代码
 * 如下：
 * // 创建 RandomAccessFile 随机访问对象
 * RandomAccessFile rFile = new RandomAccessFile("filename.txt"，"rw");
 * //获取文件流的通道（可读可写）
 * FileChannel channel = rFile.getChannel();
 * 2. 读取FileChannel通道
 * 在大部分应用场景，从通道读取数据都会调用通道的int read（ByteBufferbuf）方法，
 * 它从通道读取到数据写入到ByteBuffer缓冲区，并且返回读取到的数据量。
 * RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
 * //获取通道（可读可写）
 * FileChannel channel=aFile.getChannel();
 * //获取一个字节缓冲区
 * ByteBuffer buf = ByteBuffer.allocate(CAPACITY);
 * int length = -1;
 * //调用通道的 read 方法，读取数据并买入字节类型的缓冲区
 * while ((length = channel.read(buf)) != -1) {
 * //……省略 buf 中的数据处理
 * }
 *说明：以上代码channel.read(buf)虽然是读取通道的数据，对于通道来说是读取模式
 * 但是对于ByteBuffer缓冲区来说则是写入数据，这时，ByteBuffer缓冲区处于写入模式。
 * 3. 写入FileChannel通道
 * 写入数据到通道，在大部分应用场景，都会调用通道的write（ByteBuffer）方法，此方
 * 法的参数是一个ByteBuffer缓冲区实例，是待写数据的来源。
 * write(ByteBuffer)方法的作用，是从ByteBuffer缓冲区中读取数据，然后写入到通道自
 * 身，而返回值是写入成功的字节数。
 * //如果 buf 处于写入模式（如刚写完数据），需要 flip 翻转 buf，使其变成读取模式
 * buf.flip();
 * int outlength = 0;
 * //调用 write 方法，将 buf 的数据写入通道
 * while ((outlength = outchannel.write(buf)) != 0) {
 * System.out.println("写入的字节数：" + outlength);
 * }
 * 在以上的outchannel.write(buf)调用中，对于入参buf实例来说，需要从其中读取数据写
 * 入到outchannel通道中，所以入参buf必须处于读取模式，不能处于写入模式。
 * 4．关闭通道
 * 当通道使用完成后，必须将其关闭。关闭非常简单，调用close( )方法即可。
 * //关闭通道
 * channel.close( );
 * 5．强制刷新到磁盘
 * 在将缓冲区写入通道时，出于性能原因，操作系统不可能每次都实时将写入数据落地
 * （或刷新）到磁盘，完成最终的数据保存。
 * 如果在将缓冲数据写入通道时，需要保证数据能落地写入到磁盘，可以在写入后调用
 * 一下FileChannel的force()方法。
 * //强制刷新到磁盘
 * channel.force(true);
 */
public class FileNIOCopyDemo {
    /**
     * 演示程序的入口函数
     *
     * @param args
     */
    public static void main(String[] args) {

        nioCopyResouceFile();
        /**
         * [INFO] --- exec-maven-plugin:3.0.0:exec (default-cli) @ netty-io ---
         *   nioCopyResouceFile |>  srcPath=/F:/项目/springboot-demo/netty-io/target/classes/system.properties
         *   nioCopyResouceFile |>  destdePath=/F:/项目/springboot-demo/netty-io/target/classes//system.copy.properties
         * 写入字节数：506
         *          nioCopyFile |>  base 复制毫秒数：7
         */
    }


    /**
     * 复制两个资源目录下的文件
     */
    public static void nioCopyResouceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        Logger.debug("srcPath=" + srcPath);

        String destShortPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destdePath = IOUtil.builderResourcePath(destShortPath);
        Logger.debug("destdePath=" + destdePath);

        nioCopyFile(srcPath, destdePath);
    }


    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) {

        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        try {
            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }


            long startTime = System.currentTimeMillis();

            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outchannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outchannel = fos.getChannel();

                int length = -1;
                ByteBuffer buf = ByteBuffer.allocate(1024);
                //从输入通道读取到buf
                while ((length = inChannel.read(buf)) != -1) {

                    //翻转buf,变成成读模式
                    buf.flip();

                    int outlength = 0;
                    //将buf写入到输出的通道
                    while ((outlength = outchannel.write(buf)) != 0) {
                        System.out.println("写入字节数：" + outlength);
                    }
                    //清除buf,变成写入模式
                    buf.clear();
                }


                //强制刷新磁盘
                outchannel.force(true);
            } finally {
                IOUtil.closeQuietly(outchannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            Logger.debug("base 复制毫秒数：" + (endTime - startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.io.iodemo.socketDemo;

import com.example.io.NioDemoConfig;
import com.example.common.util.IOUtil;
import com.example.common.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Channel（通道）的主要类型:FileChannel、SocketChannel、ServerSocketChannel、DatagramChannel。
 */

/**
 *在NIO中，涉及网络连接的通道有两个：一个是SocketChannel负责连接的数据传输，
 * 另一个是ServerSocketChannel负责连接的监听。其中，NIO中的SocketChannel传输通道，与
 * OIO中的Socket类对应；NIO中的ServerSocketChannel监听通道，对应于OIO中的
 * ServerSocket类。
 * ServerSocketChannel仅仅应用于服务器端，而SocketChannel则同时处于服务器端和客
 * 户端，所以，对应于一个连接，两端都有一个负责传输的SocketChannel传输通道。
 * 无论是ServerSocketChannel，还是SocketChannel，都支持阻塞和非阻塞两种模式。如
 * 何进行模式的设置呢？调用configureBlocking方法，具体如下：
 * （1）socketChannel.configureBlocking（false）设置为非阻塞模式。
 * （2）socketChannel.configureBlocking（true）设置为阻塞模式。
 * 在阻塞模式下，SocketChannel通道的connect连接、read读、write写操作，都是同步的
 * 和阻塞式的，在效率上与Java旧的OIO的面向流的阻塞式读写操作相同。因此，在这里不
 * 介绍阻塞模式下的通道的具体操作。在非阻塞模式下，通道的操作是异步、高效率的，这
 * 也是相对于传统的OIO的优势所在。下面仅仅详细介绍在非阻塞模式下通道的打开、读写
 * 和关闭操作等操作。
 *
 *
 * 1. 获取SocketChannel传输通道
 * 在客户端，先通过SocketChannel静态方法open()获得一个套接字传输通道；然后，将
 * socket套接字设置为非阻塞模式；最后，通过connect()实例方法，对服务器的IP和端口发起
 * 连接。
 * //获得一个套接字传输通道
 * SocketChannel socketChannel = SocketChannel.open();
 * //设置为非阻塞模式
 * socketChannel.configureBlocking(false);
 * //对服务器的 IP 和端口发起连接
 * socketChannel.connect(new InetSocketAddress("127.0.0.1"，80));
 * 非阻塞情况下，与服务器的连接可能还没有真正建立，socketChannel.connect方法就返
 * 回了，因此需要不断地自旋，检查当前是否是连接到了主机：
 * while(! socketChannel.finishConnect() ){
 *  //不断地自旋、等待，或者做一些其他的事情……
 * }
 * 在服务器端，如何获取与客户端对应的传输套接字呢？
 * 在连接建立的事件到来时，服务器端的ServerSocketChannel能成功地查询出这个新连
 * 接事件，并且通过调用服务器端ServerSocketChannel监听套接字的accept()方法，来获取新
 * 连接的套接字通道：
 * //新连接事件到来，首先通过事件，获取服务器监听通道
 * ServerSocketChannel server = (ServerSocketChannel) key.channel();
 * //获取新连接的套接字通道
 * SocketChannel socketChannel = server.accept();
 * //设置为非阻塞模式
 * socketChannel.configureBlocking(false);
 * 2. 读取SocketChannel传输通道
 * 当SocketChannel传输通道可读时，可以从SocketChannel读取数据，具体方法与前面的
 * 文件通道读取方法是相同的。调用read方法，将数据读入缓冲区ByteBuffer。
 * ByteBufferbuf = ByteBuffer.allocate(1024);
 * int bytesRead = socketChannel.read(buf);
 * 在读取时，因为是异步的，因此我们必须检查read的返回值，以便判断当前是否读取
 * 到了数据。read()方法的返回值是读取的字节数，如果返回-1，那么表示读取到对方的输出
 * 结束标志，对方已经输出结束，准备关闭连接。实际上，通过read方法读数据，本身是很
 * 简单的，比较困难的是，在非阻塞模式下，如何知道通道何时是可读的呢？这就需要用到
 * NIO的新组件——Selector通道选择器，稍后介绍。
 * 3. 写入到SocketChannel传输通道
 * 和前面的把数据写入到FileChannel文件通道一样，大部分应用场景都会调用通道的int
 * write（ByteBufferbuf）方法。
 * //写入前需要读取缓冲区，要求 ByteBuffer 是读取模式
 * buffer.flip();
 * socketChannel.write(buffer);
 * 4. 关闭SocketChannel传输通道
 * 在关闭SocketChannel传输通道前，如果传输通道用来写入数据，则建议调用一次
 * shutdownOutput()终止输出方法，向对方发送一个输出的结束标志（-1）。然后调用
 * socketChannel.close()方法，关闭套接字连接。
 * //调用终止输出方法，向对方发送一个输出的结束标志
 * socketChannel.shutdownOutput();
 * //关闭套接字连接
 * IOUtil.closeQuietly(socketChannel);
 */
public class NioSendClient {
    public static void main(String[] args) {

        NioSendClient client = new NioSendClient(); // 启动客户端连接
        client.sendFile(); // 传输文件

    }
    /**
     * 构造函数
     * 与服务器建立连接
     *
     * @throws Exception
     */
    public NioSendClient() {

    }

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 向服务端传输文件
     *
     * @throws Exception
     */
    public void sendFile() {
        try {


            String sourcePath = NioDemoConfig.SOCKET_SEND_FILE;
            String srcPath = IOUtil.getResourcePath(sourcePath);
            Logger.debug("srcPath=" + srcPath);

            String destFile = NioDemoConfig.SOCKET_RECEIVE_FILE;
            Logger.debug("destFile=" + destFile);

            File file = new File(srcPath);
            if (!file.exists()) {
                Logger.debug("文件不存在");
                return;
            }
            FileChannel fileChannel = new FileInputStream(file).getChannel();

            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().connect(
                    new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                            , NioDemoConfig.SOCKET_SERVER_PORT));
            socketChannel.configureBlocking(false);
            Logger.debug("Cliect 成功连接服务端");

            while (!socketChannel.finishConnect()) {
                //不断的自旋、等待，或者做一些其他的事情
            }


            //发送文件名称
            ByteBuffer fileNameByteBuffer = charset.encode(destFile);
            socketChannel.write(fileNameByteBuffer);

            //发送文件长度
            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
            buffer.putLong(file.length());

            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();


            //发送文件内容
            Logger.debug("开始传输文件");
            int length = 0;
            long progress = 0;
            while ((length = fileChannel.read(buffer)) > 0) {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
                progress += length;
                Logger.debug("| " + (100 * progress / file.length()) + "% |");
            }

            if (length == -1) {
                IOUtil.closeQuietly(fileChannel);
                socketChannel.shutdownOutput();
                IOUtil.closeQuietly(socketChannel);
            }
            Logger.debug("======== 文件传输成功 ========");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}

package com.example.io.iodemo.socketDemo;

import com.example.io.NioDemoConfig;
import com.example.common.util.IOUtil;
import com.example.common.util.Logger;
import com.example.common.util.Print;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class NioReceiveServer {


    private Charset charset = Charset.forName("UTF-8");

    /**
     * 服务器端保存的客户端对象，对应一个客户端文件
     */
    static class Client {
        //文件名称
        String fileName;
        //长度
        long fileLength;

        //开始传输的时间
        long startTime;

        //客户端的地址
        InetSocketAddress remoteAddress;

        //输出的文件通道
        FileChannel outChannel;

    }

    private ByteBuffer buffer
            = ByteBuffer.allocate(NioDemoConfig.SERVER_BUFFER_SIZE);

    //使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
    Map<SelectableChannel, Client> clientMap = new HashMap<SelectableChannel, Client>();


    public void startServer() throws IOException {
        // 1、获取Selector选择器
        Selector selector = Selector.open();

        // 2、获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();

        // 3.设置为非阻塞
        serverChannel.configureBlocking(false);
        // 4、绑定连接
        InetSocketAddress address
                = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.bind(address);
        // 5、将通道注册到选择器上,并注册的IO事件为：“接收新连接”
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        Print.tcfo("serverChannel is linstening...");
        // 6、轮询感兴趣的I/O就绪事件（选择键集合）
        while (selector.select() > 0) {
            // 7、获取选择键集合
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                // 8、获取单个的选择键，并处理
                SelectionKey key = it.next();

                // 9、判断key是具体的什么事件，是否为新连接事件
                if (key.isAcceptable()) {
                    // 10、若接受的事件是“新连接”事件,就获取客户端新连接
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    if (socketChannel == null) continue;
                    // 11、客户端新连接，切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 12、将客户端新连接通道注册到selector选择器上
                    SelectionKey selectionKey =
                            socketChannel.register(selector, SelectionKey.OP_READ);
                    // 余下为业务处理
                    Client client = new Client();
                    client.remoteAddress
                            = (InetSocketAddress) socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel, client);
                    Logger.debug(socketChannel.getRemoteAddress() + "连接成功...");

                } else if (key.isReadable()) {
                    processData(key);
                }
                // NIO的特点只会累加，已选择的键的集合不会删除
                // 如果不删除，下一次又会被select函数选中
                it.remove();
            }
        }
    }

    /**
     * 处理客户端传输过来的数据
     */
    private void processData(SelectionKey key) throws IOException {
        Client client = clientMap.get(key.channel());

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        try {
            buffer.clear();
            while ((num = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                //客户端发送过来的，首先是文件名
                if (null == client.fileName) {

                    // 文件名
                    String fileName = charset.decode(buffer).toString();

                    String destPath = IOUtil.getResourcePath(NioDemoConfig.SOCKET_RECEIVE_PATH);
                    File directory = new File(destPath);
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    client.fileName = fileName;
                    String fullName = directory.getAbsolutePath()
                            + File.separatorChar + fileName;
                    Logger.debug("NIO  传输目标文件：" + fullName);

                    File file = new File(fullName);
                    FileChannel fileChannel = new FileOutputStream(file).getChannel();
                    client.outChannel = fileChannel;


                }
                //客户端发送过来的，其次是文件长度
                else if (0 == client.fileLength) {
                    // 文件长度
                    long fileLength = buffer.getLong();
                    client.fileLength = fileLength;
                    client.startTime = System.currentTimeMillis();
                    Logger.debug("NIO  传输开始：");
                }
                //客户端发送过来的，最后是文件内容
                else {
                    // 写入文件
                    client.outChannel.write(buffer);
                }
                buffer.clear();
            }
            key.cancel();
        } catch (IOException e) {
            key.cancel();
            e.printStackTrace();
            return;
        }
        // 调用close为-1 到达末尾
        if (num == -1) {
            IOUtil.closeQuietly(client.outChannel);
            System.out.println("上传完毕");
            key.cancel();
            Logger.debug("文件接收成功,File Name：" + client.fileName);
            Logger.debug(" Size：" + IOUtil.getFormatFileSize(client.fileLength));
            long endTime = System.currentTimeMillis();
            Logger.debug("NIO IO 传输毫秒数：" + (endTime - client.startTime));
        }
    }


    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        NioReceiveServer server = new NioReceiveServer();
        server.startServer();
    }
}

/**
 *
 * 由于客户端每次传输文件，都会分为多次传输：
 * （1）首先传入文件名称；
 * （2）其次是文件大小；
 * （3）然后是文件内容。
 * 对应于每一个客户端socketChannel，创建一个Client客户端对象，用于保存客户端状
 * 态，分别保存文件名、文件大小和写入的目标文件通道outChannel。
 * socketChannel和Client对象之间是一对一的对应关系：建立连接的时候，以键值对的形
 * 式保存Client实例在map中，其中socketChannel作为键（Key），Client对象作为值
 * （Value）。当socketChannel传输通道有数据可读时，通过选择键key.channel()方法，取出
 * IO事件所在socketChannel通道。然后通过socketChannel通道，从map中取到对应的Client对
 * 象。
 * 接收到数据时，如果文件名为空，先处理文件名称，并把文件名保存到Client对象，同
 * 时创建服务器上的目标文件；接下来再读到数据，说明接收到了文件大小，把文件大小保
 * 存到Client对象；接下来再接到数据，说明是文件内容了，则写入Client对象的outChannel文
 * 件通道中，直到数据读取完毕。
 * 运行方式：启动这个NioReceiveServer服务器程序后，再启动前面介绍的客户端程序
 * NioSendClient，即可以完成文件的传输。
 * 由于NIO传输是非阻塞的、异步的，所以，在传输过程中会出现“粘包”和“半包”
 * 问题。正因为这个原因，无论是前面NIO文件传输实例、还是Discard服务器程序，都会在
 * 传输过程中的出现异常现象（偶现）。由于以上的实例，在生产过程中不会使用，仅仅是
 * 为了大家学习NIO的知识，所以，没有为了解决“粘包”和“半包”问题而将代码编写得
 * 很复杂。
 */
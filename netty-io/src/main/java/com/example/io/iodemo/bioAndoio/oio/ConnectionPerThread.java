package com.example.io.iodemo.bioAndoio.oio;

import com.example.io.NioDemoConfig;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 1）OIO是面向流（Stream Oriented）的，NIO是面向缓冲区（Buffer Oriented）的。
 * 在面向流的OIO操作中，IO的 read() 操作总是以流式的方式顺序地从一个流
 * （Stream）中读取一个或多个字节，因此，我们不能随意地改变读取指针的位置，也不能
 * 前后移动流中的数据。
 * 而NIO中引入了Channel（通道）和Buffer（缓冲区）的概念。面向缓冲区的读取和写
 * 入，都是与Buffer进行交互。用户程序只需要从通道中读取数据到缓冲区中，或将数据从
 * 缓冲区中写入到通道中。NIO不像OIO那样是顺序操作，可以随意地读取Buffer中任意位置
 * 的数据，可以随意修改Buffer中任意位置的数据
 * 2)OIO的操作是阻塞的，而NIO的操作是非阻塞的。
 * OIO的操作是阻塞的，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一
 * 些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。例如，我们调用
 * 一个read方法读取一个文件的内容，那么调用read的线程会被阻塞住，直到read操作完成。
 * NIO如何做到非阻塞的呢？当我们调用read方法时，系统底层已经把数据准备好了，应
 * 用程序只需要从通道把数据复制到Buffer（缓冲区）就行；如果没有数据，当前线程可以
 * 去干别的事情，不需要进行阻塞等待
 * 3）OIO没有选择器（Selector）概念，而NIO有选择器的概念。
 */
public class ConnectionPerThread implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(NioDemoConfig.SOCKET_SERVER_PORT);

            /**
             * 多线程OIO的致命缺陷
             * 这里的while下socket没有处理完，接下来就阻塞了
             * 所以提出了reactor反应器
             */
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                //创建新线程来handle
                //或者，使用线程池来处理
                new Thread(handler).start();
            }
        } catch (IOException e) {
            /* 处理异常 */
        }

    }

    static class Handler implements Runnable {
        final Socket socket;

        Handler(Socket s) {
            socket = s;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    byte[] input = new byte[NioDemoConfig.SERVER_BUFFER_SIZE];
                    /* 读取数据 */
                    socket.getInputStream().read(input);
                    /* 处理业务逻辑，获取处理结果 */
                    byte[] output = null;
                    /* 写入结果 */
                    socket.getOutputStream().write(output);
                } catch (IOException ex) { /*处理异常*/ }
            }

        }
    }
}


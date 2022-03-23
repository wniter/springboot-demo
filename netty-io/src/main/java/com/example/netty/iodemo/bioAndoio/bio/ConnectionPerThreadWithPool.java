package com.example.netty.iodemo.bioAndoio.bio;



import com.example.netty.NioDemoConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 同步阻塞I/O处理（也就是BIO，Blocking I/O）的参考代码
 * 对于每一个新的网络连接，都通过线程池分配给一个专门线程去负
 * 责IO处理。每个线程都独自处理自己负责的socket连接的输入和输出。当然，服务器的监
 * 听线程也是独立的，任何的socket连接的输入和输出处理，不会阻塞到后面新socket连接的
 * 监听和建立，这样，服务器的吞吐量就得到了提升。早期版本的Tomcat服务器，就是这样
 * 实现的。
 */
class ConnectionPerThreadWithPool implements Runnable {
    public void run() {
        //线程池
        //注意，生产环境不能这么用，只能直接new 初始线程池
        ExecutorService executor = Executors.newFixedThreadPool(100);
        try {
            //服务器监听 socket
            ServerSocket serverSocket =
                    new ServerSocket(NioDemoConfig.SOCKET_SERVER_PORT);
            //主线程死循环, 等待新连接到来
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                //接收一个连接后，为 socket 连接，新建一个专属的处理器对象
                Handler handler = new Handler(socket);
                //创建新线程来 handle
                //或者，使用线程池来处理
                new Thread(handler).start();
            }
        } catch (IOException ex) { /* 处理异常 */ }
    }

    static class Handler implements Runnable {
        final Socket socket;

        Handler(Socket s) {
            socket = s;
        }

        public void run() {
            //死循环处理读写事件
            boolean ioCompleted = false;
            while (!ioCompleted) {
                try {
                    byte[] input = new byte[NioDemoConfig.SERVER_BUFFER_SIZE];
                    /* 读取数据 */
                    socket.getInputStream().read(input);
                    // 如果读取到结束标志
                    // ioCompleted= true
                    // socket.close();
                    /* 处理业务逻辑，获取处理结果 */
                    byte[] output = null;
                    /* 写入结果 */
                    socket.getOutputStream().write(output);
                } catch (IOException ex) { /*处理异常*/ }
            }
        }
    }
}
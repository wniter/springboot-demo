package com.example.netty.iodemo.bioandoio.bio;



import com.example.netty.NioDemoConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
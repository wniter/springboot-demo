package com.example.io.iodemo.ReactorModel;


import com.example.common.util.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


//EchoHandler就是负责socket连接的数据输入、业务处理、结果输出

class EchoHandler implements Runnable {
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;

    /**
     * 在传输处理器EchoHandler的构造器中，有两点比较重要：
     * （1）将新的SocketChannel传输通道，注册到了反应器Reactor类的同一个选择器中。
     * 这样保证了Reactor在查询IO事件时，能查询到Handler注册到选择器的IO事件（数据传输事
     * 件）。
     * （2）Channel传输通道注册完成后，将EchoHandler实例自身作为附件，attach到了选择
     * 键中。这样，在Reactor类分发事件（选择键）时，能执行到IOHandler的run方法，完成数
     * 据传输处理。
     * 如果由于上面的示例代码过于复杂而导致不能被快速的理解，可以参考下面的
     * EchoServer回显服务器实例，自己动手开发一个可以执行的单线程反应器实例。
     *
     * @param selector
     * @param c
     * @throws IOException
     */
    EchoHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        c.configureBlocking(false);
        //仅仅取得选择键，后设置感兴趣的IO事件
        sk = channel.register(selector, 0);

        //将Handler作为选择键的附件
        sk.attach(this);

        //第二步,注册Read就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void run() {

        try {

            if (state == SENDING) {
                //写入通道
                channel.write(byteBuffer);
                //写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                //写完后,注册read就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                //写完后,进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                //从通道读
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    Logger.info(new String(byteBuffer.array(), 0, length));
                }
                //读完后，准备开始写入通道,byteBuffer切换成读模式
                byteBuffer.flip();
                //读完后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //读完后,进入发送的状态
                state = SENDING;
            }
            //处理结束了, 这里不能关闭select key，需要重复使用
            //sk.cancel();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}


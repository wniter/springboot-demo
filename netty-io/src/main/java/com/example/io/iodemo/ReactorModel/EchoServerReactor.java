package com.example.io.iodemo.ReactorModel;

import com.example.io.NioDemoConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程Reactor模式
 * 总体来说，Reactor反应器模式有点儿类似事件驱动模式。在事件驱动模式中，当有事
 * 件触发时，事件源会将事件dispatch分发到handler处理器，由处理器负责事件处理。而反应
 * 器模式中的反应器角色，类似于事件驱动模式中的dispatcher事件分发器角色。
 * 具体来说，在反应器模式中，有Reactor反应器和Handler处理器两个重要的组件：
 * （1）Reactor反应器：负责查询IO事件，当检测到一个IO事件，将其发送给相应的
 * Handler处理器去处理。这里的IO事件，就是NIO中选择器查询出来的通道IO事件。
 * （2）Handler处理器：与IO事件（或者选择键）绑定，负责IO事件的处理。完成真正
 * 的连接建立、通道的读取、处理业务逻辑、负责将结果写出到通道等。
 */

/**
 *
 * 基于Java NIO如何实现简单的单线程版本的反应器模式呢？需要用到SelectionKey选择
 * 键的几个重要的成员方法：
 * （1）void attach(Object o) 将对象附加到选择键
 * 此方法可以将任何的Java POJO对象，作为附件添加到SelectionKey实例。这方法非常
 * 重要，因为单线程版本的Reactor反应器模式实现中，可以将Handler处理器实例，作为附件
 * 添加到SelectionKey实例。
 * （2）Object attachment() 从选择键获取附加对象
 * 此方法与attach(Object o)是配套使用的，其作用是取出之前通过attach(Object o)方法添
 * 加到SelectionKey选择键实例的附加对象。这个方法同样非常重要，当IO事件发生时，选择
 * 键将被select方法查询出来，可以直接将选择键的附件对象取出。
 * 在Reactor模式实现中，通过attachment() 方法所取出的，是之前通过attach(Object o)方
 * 法绑定的Handler实例，然后通过该Handler实例，完成相应的传输处理
 *
 * 总之，在反应器模式中，需要进行attach和attachment结合使用：在选择键注册完成之
 * 后，调用attach方法，将Handler实例绑定到选择键；当IO事件发生时，调用attachment方
 * 法，可以从选择键取出Handler实例，将事件分发到Handler处理器中，完成业务处理。
 */
//单线程Reactor反应器
public class EchoServerReactor implements Runnable {

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }

    Selector selector;
    ServerSocketChannel serverSocket;

    EchoServerReactor() throws IOException {
        //Reactor初始化
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.socket().bind(address);
        //非阻塞
        serverSocket.configureBlocking(false);

        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, AcceptorHandler
        sk.attach(new AcceptorHandler());
    }

    public void run() {
        //选择器轮询
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    SelectionKey sk = it.next();
                    dispatch(sk);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //反应器的分发事件
    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (handler != null) {
            handler.run();
        }
    }

    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                // 处理器：处理新连接
                SocketChannel channel = serverSocket.accept();
                if (channel != null)
                    new EchoHandler(selector, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

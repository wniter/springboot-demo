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
import java.util.concurrent.atomic.AtomicInteger;

//多线程Reactor反应器
class MultiThreadEchoServerReactor {
    public static void main(String[] args) throws IOException {
        MultiThreadEchoServerReactor server =
                new MultiThreadEchoServerReactor();
        server.startService();
    }
    ServerSocketChannel serverSocket;
    AtomicInteger next = new AtomicInteger(0);
    //selectors集合,引入多个selector选择器
    Selector[] selectors = new Selector[2];
    //引入多个子反应器
    SubReactor[] subReactors = null;

    /**
     * （1）引入多个选择器。
     * （2）设计一个新的子反应器（SubReactor）类，子反应器负责查询一个选择器。
     * （3）开启多个处理线程，一个处理线程负责执行一个子反应器（SubReactor）。
     * 为了提升效率，可以让SubReactor的数量和选择器的数量一致，避免多个线程负责一
     * 个选择器，导致需要进行线程同步，引起的效率降低。
     */
    MultiThreadEchoServerReactor() throws IOException {
        //初始化多个selector选择器
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.socket().bind(address);
        //非阻塞
        serverSocket.configureBlocking(false);

        //第一个selector,负责监控新连接事件
        SelectionKey sk =
                serverSocket.register(selectors[0], SelectionKey.OP_ACCEPT);
        //附加新连接处理handler处理器到SelectionKey（选择键）
        sk.attach(new AcceptorHandler());

        //第一个子反应器，一子反应器负责一个选择器
        SubReactor subReactor1 = new SubReactor(selectors[0]);
        //第二个子反应器，一子反应器负责一个选择器
        SubReactor subReactor2 = new SubReactor(selectors[1]);
        subReactors = new SubReactor[]{subReactor1, subReactor2};
    }

    private void startService() {
        // 一子反应器对应一条线程
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();
    }

    //反应器
    class SubReactor implements Runnable {
        //每条线程负责一个选择器的查询
        final Selector selector;

        public SubReactor(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();
                    while (it.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        SelectionKey sk = it.next();
                        dispatch(sk);
                    }
                    keySet.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        void dispatch(SelectionKey sk) {
            Runnable handler = (Runnable) sk.attachment();
            //调用之前attach绑定到选择键的handler处理器对象
            if (handler != null) {
                handler.run();
            }
        }
    }


    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                if (channel != null)
                    new MultiThreadEchoHandler(selectors[next.get()], channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (next.incrementAndGet() == selectors.length) {
                next.set(0);
            }
        }
    }




}

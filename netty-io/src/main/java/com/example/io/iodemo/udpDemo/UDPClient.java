package com.example.io.iodemo.udpDemo;

import com.example.io.NioDemoConfig;
import com.example.common.util.Dateutil;
import com.example.common.util.Print;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
/**
 * Channel（通道）的主要类型:FileChannel、SocketChannel、ServerSocketChannel、DatagramChannel。
 */

/**
 * DatagramChannel
 * 在Java中使用UDP协议传输数据，比TCP协议更加简单。和Socket套接字的TCP传输协
 * 议不同，UDP协议不是面向连接的协议。使用UDP协议时，只要知道服务器的IP和端口，
 * 就可以直接向对方发送数据。在Java NIO中，使用DatagramChannel数据报通道来处理UDP
 * 协议的数据传输。
 * 1. 获取DatagramChannel数据报通道
 * 获取数据报通道的方式很简单，调用DatagramChannel类的open静态方法即可。然后调
 * 用configureBlocking（false）方法，设置成非阻塞模式。
 * //获取 DatagramChannel 数据报通道
 * DatagramChannel channel = DatagramChannel.open();
 * //设置为非阻塞模式
 * datagramChannel.configureBlocking(false);
 * 如果需要接收数据，还需要调用bind方法绑定一个数据报的监听端口，具体如下：
 * //调用 bind 方法绑定一个数据报的监听端口
 * channel.socket().bind(new InetSocketAddress(18080));
 * 2. 读取DatagramChannel数据报通道数据
 * 当DatagramChannel通道可读时，可以从DatagramChannel读取数据。和前面的
 * SocketChannel读取方式不同，这里不调用read方法，而是调用receive（ByteBufferbuf）方法
 * 将数据从DatagramChannel读入，再写入到ByteBuffer缓冲区中。
 * //创建缓冲区
 * ByteBuffer buf = ByteBuffer.allocate(1024);
 * //从 DatagramChannel 读入，再写入到 ByteBuffer 缓冲区
 * SocketAddress clientAddr= datagramChannel.receive(buf);
 * 通道读取receive（ByteBufferbuf）方法虽然读取了数据到buf缓冲区，但是其返回值是
 * SocketAddress类型，表示返回发送端的连接地址（包括IP和端口）。通过receive方法读取
 * 数据非常简单，但是，在非阻塞模式下，如何知道DatagramChannel通道何时是可读的呢？
 * 和SocketChannel一样，同样需要用到NIO的新组件—Selector通道选择器，稍后介绍。
 * 3. 写入DatagramChannel数据报通道
 * 向DatagramChannel发送数据，和向SocketChannel通道发送数据的方法也是不同的。这
 * 里不是调用write方法，而是调用send方法。示例代码如下：
 * //把缓冲区翻转到读取模式
 * buffer.flip();
 * //调用 send 方法，把数据发送到目标 IP+端口
 * dChannel.send(buffer, new InetSocketAddress("127.0.0.1",18899));
 * //清空缓冲区，切换到写入模式
 * buffer.clear();
 * 由于UDP是面向非连接的协议，因此，在调用send方法发送数据的时候，需要指定接
 * 收方的地址（IP和端口）。
 * 4. 关闭DatagramChannel数据报通道
 * 这个比较简单，直接调用close()方法，即可关闭数据报通道。
 * //简单关闭即可
 * dChannel.close();
 */
public class UDPClient {

    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }
    public void send() throws IOException {
        //操作一：获取DatagramChannel数据报通道
        DatagramChannel dChannel = DatagramChannel.open();
        dChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        Scanner scanner = new Scanner(System.in);
        Print.tcfo("UDP 客户端启动成功！");
        Print.tcfo("请输入发送内容:");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((Dateutil.getNow() + " >>" + next).getBytes());
            buffer.flip();
            // 操作三：通过DatagramChannel数据报通道发送数据
            dChannel.send(buffer,
                    new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                            , NioDemoConfig.SOCKET_SERVER_PORT));
            buffer.clear();
        }
        //操作四：关闭DatagramChannel数据报通道
        dChannel.close();
    }

}

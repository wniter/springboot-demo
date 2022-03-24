package com.example.netty.basic;

/**
 * Netty通道的抽象类AbstractChannel的构造函数如下：
 * protected AbstractChannel(Channel parent) {
 * this.parent = parent; //父通道
 * id = newId();
 * unsafe = newUnsafe(); //新建一个底层的 NIO 通道,完成实际的 IO 操作
 * pipeline = newChannelPipeline(); //新建一条通道流水线
 * }
 * AbstractChannel内部有一个pipeline属性，表示处理器的流水线。Netty在对通道进行初
 * 始化的时候，将pipeline属性初始化为DefaultChannelPipeline的实例。以上代码表明，每个
 * 通道拥有一条ChannelPipeline处理器流水线。
 * AbstractChannel内部有一个parent父通道属性，保持通道的父通道。对于连接监听通道
 * （如NioServerSocketChannel）来说，其父亲通道为null；而对于传输通道（如
 * NioSocketChannel）来说，其parent属性的值为接收到该连接的监听通道。
 * 几乎所有的Netty通道实现类都继承了AbstractChannel抽象类，都拥有上面的parent和
 * pipeline两个属性成员。
 * 接下来，介绍一下通道接口中所定义的几个重要方法：
 * 方法 1. ChannelFuture connect(SocketAddress address)
 * 此方法的作用为：连接远程服务器。方法的参数为远程服务器的地址，调用后会立即
 * 返回，其返回值为执行连接操作的异步任务ChannelFuture。此方法在客户端的传输通道使
 * 方法 2. ChannelFuture bind（SocketAddress address）
 * 此方法的作用为：绑定监听地址，开始监听新的客户端连接。此方法在服务器的新连
 * 接监听和接收通道使用。
 * 方法 3. ChannelFuture close()
 * 此方法的作用为：关闭通道连接，返回连接关闭的ChannelFuture异步任务。如果需要
 * 在连接正式关闭后执行其他操作，则需要为异步任务设置回调方法；或者调用
 * ChannelFuture异步任务的sync( ) 方法来阻塞当前线程，一直等到通道关闭的异步任务执行
 * 完毕。
 * 方法 4. Channel read()
 * 此方法的作用为：读取通道数据，并且启动入站处理。具体来说，从内部的Java NIO
 * Channel通道读取数据，然后启动内部的Pipeline流水线，开启数据读取的入站处理。此方
 * 法的返回通道自身用于链式调用。
 * 方法 5. ChannelFuture write（Object o）
 * 此方法的作用为：启程出站流水处理，把处理后的最终数据写到底层通道（如Java
 * NIO通道）。此方法的返回值为出站处理的异步处理任务。
 * 方法 6. Channel flush()
 * 此方法的作用为：将缓冲区中的数据立即写出到对端。调用前面的write(…)出站处理
 * 时，并不能将数据直接写出到对端，write操作的作用在大部分情况下仅仅是写入到操作系
 * 统的缓冲区，操作系统会将根据缓冲区的情况，决定什么时候把数据写到对端。而执行
 * flush()方法立即将缓冲区的数据写到对端。
 * 上面的6种方法，仅仅是比较常见的通道方法。在Channel接口中以及各种通道的实现
 * 类中，
 */
//public class Channel {
//}

package com.example.netty.basic;
//
//public class Bootstrap {
//}
/**
 Bootstrap类是Netty提供的一个便利的工厂类，可以通过它来完成Netty的客户端或服务
 器端的Netty组件的组装，以及Netty程序的初始化和启动执行。

 Bootstrap的启动流程，也就是Netty组件的组装、配置，以及Netty服务器或者客户端的
 启动流程。在本节中对启动流程进行了梳理，大致分成了8个步骤。本书仅仅演示的是服务
 器端引导类的使用，用到的引导类为ServerBootstrap。正式使用前，首先创建一个服务器端
 的引导类实例。
 //创建一个服务器端的引导类
 ServerBootstrap b = new ServerBootstrap();
 接下来，结合前面的NettyDiscardServer服务器的程序代码，给大家详细介绍一下
 Bootstrap启动流程中精彩的8个步骤。
 第1步：创建反应器轮询组，并设置到ServerBootstrap引导类实例，大致的代码如下：
 //创建反应器轮询组
 //boss 轮询组
 EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
 //worker 轮询组
 EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
 //...
 //step1：为引导类实例设置反应器轮询组
 b.group(bossLoopGroup, workerLoopGroup);
 在设置反应器轮询组之前，创建了两个NioEventLoopGroup轮询组，一个负责处理连接
 监听IO事件，名为bossLoopGroup；另一个负责数据传输事件和处理，名为
 workerLoopGroup。在两个轮询组创建完成后，就可以配置给引导类实例，它一次性地给引
 导类配置了两大轮询组。
 如果不需要进行新连接事件和输出事件进行分开监听，就不一定非得配置两个轮询
 组，可以仅配置一个EventLoopGroup反应器轮询组。具体的配置方法是调用
 b.group( workerGroup)。在这种模式下，新连接监听IO事件和数据传输IO事件可能被挤在了
 同一个线程中处理。这样会带来一个风险：新连接的接受被更加耗时的数据传输或者业务
 处理所阻塞。所以，在服务器端，建议设置成两个轮询组的工作模式。
 第2步：设置通道的IO类型。Netty不止支持Java NIO，也支持阻塞式的OIO（也叫
 BIO，Block-IO，即阻塞式IO）。下面配置的是Java NIO类型的通道类型，大致如下：
 //step2：设置传输通道的类型为 nio 类型
 b.channel(NioServerSocketChannel.class);
 如果确实指定Bootstrap的IO模型为BIO类型，可以配置为OioServerSocketChannel.class
 类即可。由于NIO的优势巨大，通常不会在Netty中使用BIO。 第3步：设置监听端口，大致的代码如下：
 //step3：设置监听端口
 b.localAddress(new InetSocketAddress(port));
 这是最为简单的一步操作，主要是设置服务器的监听地址。
 第4步：设置传输通道的配置选项，大致的代码如下：
 //step4：设置通道的参数
 b.option(ChannelOption.SO_KEEPALIVE, true);
 b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
 这里用到了Bootstrap的option(…) 选项设置方法。对于服务器的Bootstrap而言，这个方
 法的作用是：给父通道（Parent Channel）通道设置一些与传输协议相关的选项。如果要给
 子通道（Child Channel）设置一些通道选项，则需要用另外一个childOption(…)设置方法。
 可以设置哪些通道选项（ChannelOption）呢？在上面的代码中，设置了一个底层TCP
 相关的选项ChannelOption.SO_KEEPALIVE。该选项表示：是否开启TCP底层心跳机制，
 true为开启，false为关闭。其他的通道设置选项，参见下一小节。
 第5步：装配子通道的Pipeline流水线。上一节介绍到，每一个通道都用一条
 ChannelPipeline流水线。它的内部有一个双向的链表。装配流水线的方式是：将业务处理
 器ChannelHandler实例包装之后加入双向链表中。
 如何装配Pipeline流水线呢？装配子通道的Handler流水线调用引导类的childHandler()方
 法，该方法需要传入一个ChannelInitializer通道初始化类的实例作为参数。每当父通道成功
 接收一个连接，并创建成功一个子通道后，就会初始化子通道，此时这里配置的
 ChannelInitializer实例就会被调用。
 在ChannelInitializer通道初始化类的实例中，有一个initChannel初始化方法，在子通道
 创建后会被执行到，向子通道流水线增加业务处理器。
 装配子通道的Pipeline流水线的大致代码如下：
 //step5：装配子通道流水线
 b.childHandler(new ChannelInitializer<SocketChannel>() {
 //有连接到达时会创建一个通道的子通道，并初始化
 protected void initChannel(SocketChannel ch)...{
 // 这里可以管理子通道中的 Handler 业务处理器
 // 向子通道流水线添加一个 Handler 业务处理器
 ch.pipeline().addLast(new NettyDiscardHandler());
 }
 });
 为什么仅装配子通道的流水线，而不需要装配父通道的流水线呢？原因是：父通道也
 就是NioServerSocketChannel的内部业务处理是固定的：接受新连接后，创建子通道，然后
 初始化子通道，所以不需要特别的配置，由Netty自行进行装配。当然，如果需要完成特殊
 的父通道业务处理，可以类似的使用ServerBootstrap的handler(ChannelHandler handler)方
 法，为父通道设置ChannelInitializer初始化器。
 在装配流水线时需要注意的是，ChannelInitializer处理器器有一个泛型参数
 SocketChannel，它代表需要初始化的通道类型，这个类型需要和前面的引导类中设置的传
 输通道类型，保持一一对应起来。
 第6步：开始绑定服务器新连接的监听端口，大致的代码如下：
 //step6：开始绑定端口，通过调用 sync 同步方法阻塞直到绑定成功
 ChannelFuture channelFuture = b.bind().sync();
 Logger.info(" 服务器启动成功，监听端口: " +
 channelFuture.channel().localAddress());
 这个也很简单。b.bind()方法的功能：返回一个端口绑定Netty的异步任务
 channelFuture。在这里，并没有给channelFuture异步任务增加回调监听器，而是阻塞
 channelFuture异步任务，直到端口绑定任务执行完成。
 在Netty中，所有的IO操作都是异步执行的，这就意味着任何一个IO操作会立刻返回，
 在返回的时候，异步任务还没有真正执行。什么时候执行完成呢？Netty中的IO操作，都会
 返回异步任务实例（如ChannelFuture实例）。通过该异步任务实例，既可以实现同步阻塞
 一直到ChannelFuture异步任务执行完成，也可以为其增加事件监听器的方式注册异步回调
 逻辑，以获得Netty中的IO操作的真正结果。而上面所使用的，是同步阻塞一直到
 ChannelFuture异步任务执行完成的处理方式。
 至此，服务器正式启动。
 第7步：自我阻塞，直到监听通道关闭，大致的代码如下：
 //step7：自我阻塞，直到通道关闭的异步任务结束
 ChannelFuture closeFuture = channelFuture.channel().closeFuture();
 closeFuture.sync();
 如果要阻塞当前线程直到通道关闭，可以使用通道的closeFuture()方法，以获取通道关
 闭的异步任务。当通道被关闭时，closeFuture实例的sync()方法会返回。
 第8步：关闭EventLoopGroup，大致的代码如下：
 //step8：释放掉所有资源，包括创建的反应器线程
 workerLoopGroup.shutdownGracefully();
 bossLoopGroup.shutdownGracefully();
 关闭Reactor反应器轮询组，同时会关闭内部的SubReactor子反应器线程，也会关闭内
 部的Selector选择器、内部的轮询线程以及负责查询的所有的子通道。在子通道关闭后，会
 释放掉底层的资源，如Socket文件描述符等。


 ChannelOption通道选项
 1. SO_RCVBUF和SO_SNDBUF
 此为TCP传输选项，每个TCP socket（套接字）在内核中都有一个发送缓冲区和一个接
 收缓冲区，这两个选项就是用来设置TCP连接的这两个缓冲区大小的。TCP的全双工工作
 模式以及TCP的滑动窗口对两个独立的缓冲区都有依赖。
 2. TCP_NODELAY
 此为TCP传输选项，如果设置为true表示立即发送数据。TCP_NODELAY就是用于启用
 或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true
 （关闭Nagle算法）；如果要减少发送次数减少网络交互，就设置为false（启用Nagle算
 法），等累积一定大小的数据后再发送。TCP_NODELAY的值Netty默认为True，而操作系
 统默认为False。
 Nagle算法将小的碎片数据连接成更大的报文（或数据包）来最小化所发送报文的数
 量，如果需要发送一些较小的报文，则需要禁用该算法。
 Netty默认禁用Nagle算法，报文会立即发送出去，从而最小化报文传输的延时。
 3. SO_KEEPALIVE
 此为TCP传输选项，表示是否开启TCP协议的心跳机制。true为连接保持心跳，默认值
 为false。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心
 跳机制，需要注意的是：默认的心跳间隔是7200秒即2小时。Netty默认关闭该功能。
 4. SO_REUSEADDR
 此为TCP传输选项，如果为true时表示地址复用，默认值为false。有四种情况需要用到
 这个参数设置：
 ⚫ 当有一个地址和端口相同的连接socket1处于TIME_WAIT状态时，而又希望启动一
 个新的连接socket2要占用该地址和端口；
 ⚫ 有多块网卡或用IP Alias技术的机器在同一端口启动多个进程，但每个进程绑定的
 本地IP地址不能相同；
 ⚫ 同一进程绑定相同的端口到多个socket（套接字）上，但每个socket绑定的IP地址
 不同；
 ⚫ 完全相同的地址和端口的重复绑定，但这只用于UDP的多播，不用于TCP。
 5. SO_LINGER
 此为TCP传输选项，选项可以用来控制socket.close()方法被调用后的行为，包括延迟关
 闭时间。如果此选项设置为-1，表示socket.close()方法在调用后立即返回，但操作系统底层
 会将发送缓冲区的数据全部发送到对端；如果此选项设置为0，表示socket.close()方法在调
 用后会立即返回，但是操作系统会放弃发送缓冲区数据，而是直接向对端发送RST包，对
 端将收到复位错误；如果此选项设置为非0整数值，表示调用socket.close()方法的线程被阻
 塞，直到延迟时间到来，发送缓冲区中的数据发送完毕，若超时，则对端会收到复位错
 误。
 SO_LINGER的默认值为-1，表示禁用该功能。
 6. SO_BACKLOG
 此为TCP传输选项，表示服务器端接收连接的队列长度，如果队列已满，客户端连接
 将被拒绝。服务端在处理客户端新连接请求时（三次握手），是顺序处理的，所以同一时
 间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求
 放在队列中等待处理，队列的大小通过SO_BACKLOG指定。
 具体来说，服务端对完成第二次握手的连接放在一个队列（暂时称A队列），如果进
 一步完成第三次握手，再把的连接从A队列移动到新队列（暂时称B队列），接下来应用程
 序会通过accept方法取出握手成功的连接，而系统则会将该连接从B队列移除。 A队列和B
 队列的长度之和是SO_BACKLOG指定的值，当A和B队列的长度之和大于SO_BACKLOG
 值时，新连接将会被TCP内核拒绝，所以，如果SO_BACKLOG过小，可能会出现accept速
 度跟不上，A和.B两队列满了，导致新客户端无法连接
 如果连接建立频繁，服务器处理新连接较慢，可以适当调大这个参数。
 7. SO_BROADCAST
 此为TCP传输选项，表示设置为广播模式。
*/


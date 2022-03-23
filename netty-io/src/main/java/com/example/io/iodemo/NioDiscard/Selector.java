package com.example.io.iodemo.NioDiscard;

//public class Selector {
//}
/**
 选择器（Selector）是什么呢？选择器和通道的关系又是什么？
 简单地说：选择器的使命是完成IO的多路复用，其主要工作是通道的注册、监听、事
 件查询。一个通道代表一条连接通路，通过选择器可以同时监控多个通道的IO（输入输
 出）状况。选择器和通道的关系，是监控和被监控的关系。
 选择器提供了独特的API方法，能够选出（select）所监控的通道已经发生了哪些IO事
 件，包括读写就绪的IO操作事件。
 在NIO编程中，一般是一个单线程处理一个选择器，一个选择器可以监控很多通道。
 所以，通过选择器，一个单线程可以处理数百、数千、数万、甚至更多的通道。在极端情
 况下（数万个连接），只用一个线程就可以处理所有的通道，这样会大量地减少线程之间
 上下文切换的开销。
 通道和选择器之间的关联，通过register（注册）的方式完成。调用通道的
 Channel.register（Selector sel，int ops）方法，可以将通道实例注册到一个选择器中。
 register方法有两个参数：第一个参数，指定通道注册到的选择器实例；第二个参数，指定
 选择器要监控的IO事件类型。
 可供选择器监控的通道IO事件类型，包括以下四种：
 （1）可读：SelectionKey.OP_READ
 （2）可写：SelectionKey.OP_WRITE
 （3）连接：SelectionKey.OP_CONNECT
 （4）接收：SelectionKey.OP_ACCEPT
 以上的事件类型常量定义在SelectionKey类中。如果选择器要监控通道的多种事件，可
 以用“按位或”运算符来实现。例如，同时监控可读和可写IO事件：
 //监控通道的多种事件，用“按位或”运算符来实现
 int key = SelectionKey.OP_READ | SelectionKey.OP_WRITE ;
 什么是IO事件呢？
 这个概念容易混淆，这里特别说明一下。这里的IO事件不是对通道的IO操作，而是通
 道处于某个IO操作的就绪状态，表示通道具备执行某个IO操作的条件。比方说某个
 SocketChannel传输通道，如果完成了和对端的三次握手过程，则会发生“连接就绪”
 （OP_CONNECT）的事件。再比方说某个ServerSocketChannel服务器连接监听通道，在监
 听到一个新连接的到来时，则会发生“接收就绪”（OP_ACCEPT）的事件。还比方说，
 一个SocketChannel通道有数据可读，则会发生“读就绪”（OP_READ）事件；一个等待
 写入数据的SocketChannel通道，会发生写就绪（OP_WRITE）事件.

 SelectableChannel可选择通道
 并不是所有的通道，都是可以被选择器监控或选择的。比方说，FileChannel文件通道
 就不能被选择器复用。判断一个通道能否被选择器监控或选择，有一个前提：判断它是否
 继承了抽象类SelectableChannel（可选择通道），如果是则可以被选择，否则不能。
 简单地说，一条通道若能被选择，必须继承SelectableChannel类。
 SelectableChannel类，是何方神圣呢？它提供了实现通道的可选择性所需要的公共方
 法。Java NIO中所有网络链接Socket套接字通道，都继承了SelectableChannel类，都是可选
 择的。而FileChannel文件通道，并没有继承SelectableChannel，因此不是可选择通道。

 SelectionKey选择键
 通道和选择器的监控关系注册成功后，就可以选择就绪事件。具体的选择工作，和调
 用选择器Selector的select( )方法来完成。通过select方法，选择器可以不断地选择通道中所
 发生操作的就绪状态，返回注册过的感兴趣的那些IO事件。换句话说，一旦在通道中发生
 了某些IO事件（就绪状态达成），并且是在选择器中注册过的IO事件，就会被选择器选
 中，并放入SelectionKey选择键的集合中。
 这里出现一个新的概念——SelectionKey选择键。SelectionKey选择键是什么呢？简单
 地说，SelectionKey选择键就是那些被选择器选中的IO事件。前面讲到，一个IO事件发生
 （就绪状态达成）后，如果之前在选择器中注册过，就会被选择器选中，并放入
 SelectionKey选择键集合中；如果之前没有注册过，即使发生了IO事件，也不会被选择器选
 中。SelectionKey选择键和IO的关系，可以简单地理解为：选择键，就是被选中了的IO事
 件。
 在实际编程时，选择键的功能是很强大的。通过SelectionKey选择键，不仅仅可以获得
 通道的IO事件类型，比方说SelectionKey.OP_READ；还可以获得发生IO事件所在的通道；
 另外，也可以获得选出选择键的选择器实例。

 */


/**
 选择器使用流程
 使用选择器，主要有以下三步：
 （1）获取选择器实例；
 （2）将通道注册到选择器中；
 （3）轮询感兴趣的IO就绪事件（选择键集合）。
 第一步：获取选择器实例。选择器实例是通过调用静态工厂方法open()来获取的，具
 体如下：
 //调用静态工厂方法 open()来获取 Selector 实例
 Selector selector = Selector.open();
 Selector选择器的类方法open( )的内部，是向选择器SPI（SelectorProvider）发出请求，
 通过默认的SelectorProvider（选择器提供者）对象，获取一个新的选择器实例。Java中SPI
 全称为（Service Provider Interface，服务提供者接口），是JDK的一种可以扩展的服务提供
 和发现机制。Java通过SPI的方式，提供选择器的默认实现版本。也就是说，其他的服务提
 供商可以通过SPI的方式，提供定制化版本的选择器的动态替换或者扩展。
 第二步：将通道注册到选择器实例。要实现选择器管理通道，需要将通道注册到相应
 的选择器上，简单的示例代码如下：
 // 2.获取通道
 ServerSocketChannelserverSocketChannel = ServerSocketChannel.open();
 // 3.设置为非阻塞
 serverSocketChannel.configureBlocking(false);
 // 4.绑定连接
 serverSocketChannel.bind(new InetSocketAddress(18899));
 // 5.将通道注册到选择器上,并制定监听事件为：“接收连接”事件
 serverSocketChannel.register(selector，SelectionKey.OP_ACCEPT);
 上面通过调用通道的register(…)方法，将ServerSocketChannel通道注册到了一个选择器
 上。当然，在注册之前，首先需要准备好通道。
 这里需要注意：注册到选择器的通道，必须处于非阻塞模式下，否则将抛出
 IllegalBlockingModeException异常。这意味着，FileChannel文件通道不能与选择器一起使
 用，因为FileChannel文件通道只有阻塞模式，不能切换到非阻塞模式；而Socket套接字相
 关的所有通道都可以。
 其次，还需要注意：一个通道，并不一定要支持所有的四种IO事件。例如服务器监听
 通道ServerSocketChannel，仅仅支持Accept（接收到新连接）IO事件；而传输通道
 SocketChannel则不同，该类型通道不支持Accept类型的IO事件。
 如何判断通道支持哪些事件呢？可以在注册之前，可以通过通道的validOps()方法，来
 获取该通道所有支持的IO事件集合。
 第三步：选出感兴趣的IO就绪事件（选择键集合）。通过Selector选择器的select()方
 法，选出已经注册的、已经就绪的IO事件，并且保存到SelectionKey选择键集合中。
 SelectionKey集合保存在选择器实例内部，其元素为SelectionKey类型实例。调用选择器的
 selectedKeys()方法，可以取得选择键集合。
 接下来，需要迭代集合的每一个选择键，根据具体IO事件类型，执行对应的业务操
 作。大致的处理流程如下：
 //轮询，选择感兴趣的 IO 就绪事件（选择键集合）
 while (selector.select() > 0) {
 Set selectedKeys = selector.selectedKeys();
 Iterator keyIterator = selectedKeys.iterator();
 while(keyIterator.hasNext()) {
 SelectionKey key = keyIterator.next();
 //根据具体的 IO 事件类型，执行对应的业务操作
 if(key.isAcceptable()) {
 // IO 事件：ServerSocketChannel 服务器监听通道有新连接
 } else if (key.isConnectable()) {
 // IO 事件：传输通道连接成功
 } else if (key.isReadable()) {
 // IO 事件：传输通道可读
 } else if (key.isWritable()) {
 // IO 事件：传输通道可写
 }
 //处理完成后，移除选择键
 keyIterator.remove();
 }}
 处理完成后，需要将选择键从这个SelectionKey集合中移除，防止下一次循环的时候，
 被重复的处理。SelectionKey集合不能添加元素，如果试图向SelectionKey选择键集合中添
 加元素，则将抛出java.lang.UnsupportedOperationException异常。
 用于选择就绪的IO事件的select()方法，有多个重载的实现版本，具体如下：
 （1）select()：阻塞调用，一直到至少有一个通道发生了注册的IO事件。
 （2）select(long timeout)：和select()一样，但最长阻塞时间为timeout指定的毫秒数。
 （3）selectNow()：非阻塞，不管有没有IO事件，都会立刻返回。
 select()方法的返回值的是整数类型（int），表示发生了IO事件的数量。更准确地说，
 是从上一次select到这一次select之间，有多少通道发生了IO事件，更加准确地说，是指发
 生了选择器感兴趣（注册过）的IO事件数。
 */
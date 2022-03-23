//package com.example.spring.jedis;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class SpringJedisApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(SpringJedisApplication.class, args);
//    }
//
//}
//Redis的主要应用场景：缓存（数据查询、短连接、新闻内容、商品内容等）、分布式
//会话（Session）、聊天室的在线好友列表、任务队列（秒杀、抢购、12306等）、应用排
//行榜、访问统计、数据过期处理（可以精确到毫秒）。

//Redis成为缓存事实标准的原因
//        （1）速度快 不需要等待磁盘的IO，在内存之间进行的数据存储和查询，速度非常
//        快。当然，缓存的数据总量不能太大，因为受到物理内存空间大小的限制。
//        （2）丰富的数据结构 除了string之外，还有list、hash、set、sortedset，一共五种类
//        型。
//        （3）单线程，避免了线程切换和锁机制的性能消耗。
//        （4）可持久化 支持RDB与AOF两种方式，将内存中的数据写入外部的物理存储设
//        备。
//        （5）支持发布/订阅。
//        （6）支持Lua脚本。
//        （7）支持分布式锁 在分布式系统中，如果不同的节点需要访同到一个资源，往往需
//        要通过互斥机制来防止彼此干扰，并且保证数据的一致性。在这种情况下，需要使用到分
//        布式锁。分布式锁和Java的锁用于实现不同线程之间的同步访问，原理上是类似的。
//        （8）支持原子操作和事务 Redis事务是一组命令的集合。一个事务中的命令要么都
//        执行，要么都不执行。如果命令在运行期间出现错误，不会自动回滚。
//        （9）支持主-从（Master-Slave）复制与高可用（Redis Sentinel）集群（3.0 版本以
//        上）
//        （10）支持管道 Redis管道是指客户端可以将多个命令一次性发送到服务器，然后由
//        服务器一次性返回所有结果。管道技术的优点是：在批量执行命令的应用场景中，可以大
//        大减少网络传输的开销，提高性能。
package com.example.spring.jedis.jedisPool;

import com.example.spring.jedis.config.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 在使用JedisPool类创建Jedis连接池之前，首先要了解其配置类——JedisPoolConfig配置
 * 类，它也位于redis.clients.jedis包中。这个配置类负责配置JedisPool的参数。JedisPoolConfig
 * 配置类涉及到很多与连接管理和使用有关的参数，下面将对它的一些重要参数进行说明。
 * （1）maxTotal：资源池中最大的连接数，默认值为8。 （2）maxIdle：资源池允许最大空闲的连接数，默认值为8。
 * （3）minIdle：资源池确保最少空闲的连接数，默认值为0。如果JedisPool开启了空闲
 * 连接的有效性检测，如果空闲连接无效，就销毁。销毁连接后，连接数量就少了，如果小
 * 于minIdle数量，就新建连接，维护数量不少于minIdle的数量。minIdle确保了线程池中有最
 * 小的空闲Jedis实例的数量。
 * （4）blockWhenExhausted：当资源池用尽后，调用者是否要等待，默认值为true。当
 * 为true时，maxWaitMillis才会生效。
 * （5）maxWaitMillis：当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。
 * 默认值为-1，表示永不超时，不建议使用默认值。
 * （6）testOnBorrow：向资源池借用连接时，是否做有效性检测（ping命令），如果是
 * 无效连接，会被移除，默认值为false，表示不做检测。如果为true，则得到的Jedis实例均是
 * 可用的。在业务量小的应用场景，建议设置为true，确保连接可用；在业务量很大的应用
 * 场景，建议设置为false（默认值），少一次ping命令的开销，有助于提升性能。
 * （7）testOnReturn：向资源池归还连接时，是否做有效性检测（ping命令），如果是
 * 无效连接，会被移除，默认值为false，表示不做检测。同样，在业务量很大的应用场景，
 * 建议设置为false（默认值），少一次ping命令的开销。
 * （8）testWhileIdle：如果为true，表示用一个专门的线程对空闲的连接进行有效性的检
 * 测扫描，如果连接的有效性检测失败，即表示监测到无效连接，会从资源池中移除。默认
 * 值为true，表示进行空闲连接的检测。这个选项存在一个附加条件，需要空闲扫描间隔时
 * 间配置项timeBetweenEvictionRunsMillis的值大于0；否则，testWhileIdle不会生效。
 * （9）timeBetweenEvictionRunsMillis：表示两次空闲连接扫描的间隔时间，默认为
 * 30000毫秒，也就是30秒钟。
 * （10）minEvictableIdleTimeMillis：表示一个Jedis连接至少停留在空闲状态的最短时
 * 间，然后才能被空闲连接扫描线程进行有效性检测，默认值为60000毫秒，即60秒。也就是
 * 说在默认情况下，一条Jedis连接只有在空闲60秒后，才会参与空闲线程的有效性检测。这
 *个选项存在一个附加条件，需要在timeBetweenEvictionRunsMillis大于0时才会生效。也就
 * 是说，如果不启动空闲检测线程，这个参数也没有什么意义。
 * （11）numTestsPerEvictionRun：表示空闲检测线程每次最多扫描的Jedis连接数，默认
 * 值为-1，表示扫描全部的空闲连接。
 * 空闲扫描的选项在JedisPoolConfig的构造器中都有默认值，具体如下：
 * package redis.clients.jedis;
 * import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
 * public class JedisPoolConfig extends GenericObjectPoolConfig {
 *  public JedisPoolConfig() {
 *  this.setTestWhileIdle(true);
 *  this.setMinEvictableIdleTimeMillis(60000L);
 *  this.setTimeBetweenEvictionRunsMillis(30000L);
 *  this.setNumTestsPerEvictionRun(-1);
 *  } }（12）jmxEnabled：是否开启JMX监控，默认值为true，建议开启。
 * 有个实际的问题：如何推算一个连接池的最大连接数maxTotal呢？实际上，这是一个
 * 很难精准回答的问题，主要是依赖的因素比较多。大致的推算方法是：业务QPS/单连接的
 * QPS =最大连接数。
 * 如何推算单个Jedis连接的QPS呢？假设一个Jedis命令操作的时间约为5ms（包含borrow
 * + return + Jedis执行命令 + 网络延迟），那么，单个Jedis连接的QPS大约是1000/5 =200。
 * 如果业务期望的QPS是100000，则需要的最大连接数为100000/200 =500。
 * 事实上，上面的估算仅仅是个理论值。在实际的生产场景中，还要预留一些资源，通
 * 常来讲所配置的maxTotal要比理论值大一些。
 * 如果连接数确实太多，可以考虑Redis集群，那么单个Redis节点的最大连接数的公式
 * 为：maxTotal = 预估的连接数 / nodes节点数。
 */
public class JredisPoolBuilder {

    public static final int MAX_IDLE = 50;
    public static final int MAX_TOTAL = 50;
    private static JedisPool pool = null;

    static {
        //创建连接池
        buildPool();
        //预热连接池
        hotPool();
    }

    //创建连接池
    private static JedisPool buildPool() {
        if (pool == null) {
            long start = System.currentTimeMillis();
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(1000 * 10);
            // 在borrow一个jedis实例时，是否提前进行validate操作；
            // 如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            pool = new JedisPool(config, "127.0.0.1", 6379, 10000);
            long end = System.currentTimeMillis();
            Logger.info("buildPool  毫秒数:", end - start);
        }
        return pool;
    }

    //获取连接
    public synchronized static Jedis getJedis() {
        return pool.getResource();
    }

    //连接池的预热
    public static void hotPool() {

        long start = System.currentTimeMillis();
        List<Jedis> minIdleJedisList = new ArrayList<Jedis>(MAX_IDLE);
        Jedis jedis = null;

        for (int i = 0; i < MAX_IDLE; i++) {
            try {
                jedis = pool.getResource();
                minIdleJedisList.add(jedis);
                jedis.ping();
            } catch (Exception e) {
                Logger.error(e.getMessage());
            } finally {
            }
        }

        for (int i = 0; i < MAX_IDLE; i++) {
            try {
                jedis = minIdleJedisList.get(i);
                jedis.close();
            } catch (Exception e) {
                Logger.error(e.getMessage());
            } finally {

            }
        }
        long end = System.currentTimeMillis();
        Logger.info("hotPool  毫秒数:", end - start);

    }


}
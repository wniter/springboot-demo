package com.example.springboot.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * @create 2022-01-17 0:49
 */
public class JedisTest {

    @Test
    public void JedisTest01() {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("192.168.31.131", 6379);
        jedis.auth("123456");
        System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        System.out.println("Server is running: " + jedis.ping());
    }

    //    Redis Java String 实例
    @Test
    public void JedisTest02() {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("192.168.31.131", 6379);
        jedis.auth("123456");
        System.out.println("Connection to server sucessfully");
        //set the data in redis string
        jedis.set("tutorial-name", "Redis tutorial");
        // Get the stored data and print it
        System.out.println("Stored string in redis:: " + jedis.get("tutorial-name"));
    }

    //Redis Java List 实例

    @Test
    public void JedisTest03() {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("192.168.31.131", 6379);
        jedis.auth("123456");
        System.out.println("Connection to server sucessfully");

        //store data in redis list
        jedis.lpush("tutorial-list", "Redis");
        jedis.lpush("tutorial-list", "Mongodb");
        jedis.lpush("tutorial-list", "Mysql");
        // Get the stored data and print it
        List<String> list = jedis.lrange("tutorial-list", 0, 5);

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Stored string in redis:: " + list.get(i));
        }
    }

    //    Redis Java Keys 实例
    @Test
    public void JedisTest04() {
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis("192.168.31.131", 6379);
        jedis.auth("123456");
        System.out.println("Connection to server sucessfully");
        //store data in redis list
        // Get the stored data and print it

        Set<String> set = jedis.keys("*");

        for (String setkey : set) {
            System.out.println("Set of stored keys:: " + setkey);
        }
    }

}

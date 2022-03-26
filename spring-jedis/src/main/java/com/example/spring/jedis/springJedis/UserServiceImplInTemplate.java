package com.example.spring.jedis.springJedis;


import com.example.spring.jedis.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * 一般来说，在普通的CRUD场景中，大致涉及到的缓存操作为：
 * 1. 创建缓存
 * 在创建（Create）一个POJO实例的时候，对POJO实例进行分布式缓存，一般以“缓存
 * 前缀+ID”为缓存的Key键，POJO对象为缓存的Value值，直接缓存POJO的二进制字节。前
 * 提是：POJO必须可序列化，实现java.io.Serializable空接口。如果POJO不可序列化，也是可
 * 以缓存的，但是必须自己实现序列化的方式，例如使用JSON方式序列化。
 * 2. 查询缓存
 * 在查询（Retrieve）一个POJO实例的时候，首先应该根据POJO缓存的Key键，从Redis
 * 缓存中返回结果。如果不存在，才去查询数据库，并且能够将数据库的结果缓存起来。
 * 3. 更新缓存
 * 在更新（Update）一个POJO实例的时候，既需要更新数据库的POJO数据记录，也需
 * 要更新POJO的缓存记录。
 * 4. 删除缓存
 * 在删除（Delete）一个POJO实例的时候，既需要删除数据库的POJO数据记录，也需要
 * 删除POJO的缓存记录。
 * 使用spring-data-redis开源库可以快速地完成上述的缓存CRUD操作。
 */

public class UserServiceImplInTemplate implements UserService {

    public static final String USER_UID_PREFIX = "user:uid:";

    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final long CASHE_LONG = 60 * 4;//4分钟

    /**
     * CRUD 之  新增/更新
     *
     * @param user 用户
     */
    @Override
    public User saveUser(final User user) {
        //保存到缓存
        redisTemplate.execute(new RedisCallback<User>() {

            @Override
            public User doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] key = serializeKey(USER_UID_PREFIX + user.getUid());
                connection.set(key, serializeValue(user));
                connection.expire(key, CASHE_LONG);
                return user;
            }
        });
        //保存到数据库
        //...如mysql
        return user;
    }

    private byte[] serializeValue(User s) {
        return redisTemplate
                .getValueSerializer().serialize(s);
    }

    private byte[] serializeKey(String s) {
        return redisTemplate
                .getKeySerializer().serialize(s);
    }

    private User deSerializeValue(byte[] b) {
        return (User) redisTemplate
                .getValueSerializer().deserialize(b);
    }

    /**
     * CRUD 之   查询
     *
     * @param id id
     * @return 用户
     */
    @Override
    public User getUser(final long id) {
        //首先从缓存中获取
        User value =  (User) redisTemplate.execute(new RedisCallback<User>() {
            @Override
            public User doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] key = serializeKey(USER_UID_PREFIX + id);
                if (connection.exists(key)) {
                    byte[] value = connection.get(key);
                    return deSerializeValue(value);
                }

                return null;
            }
        });
        if (null == value) {
            //如果缓存中没有，从数据库中获取
            //...如mysql
            //并且，保存到缓存
        }
        return value;
    }

    /**
     * CRUD 之 删除
     * @param id id
     */
    @Override
    public void deleteUser(long id) {
        //从缓存删除
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] key = serializeKey(USER_UID_PREFIX + id);
                if (connection.exists(key)) {
                    connection.del(key);
                }
                return true;
            }
        });
        //从数据库删除
        //...如mysql
    }

    /**
     * 删除全部
     */
    @Override
    public void deleteAll() {

    }


}
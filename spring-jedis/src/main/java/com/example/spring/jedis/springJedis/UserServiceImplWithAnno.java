package com.example.spring.jedis.springJedis;


import com.example.spring.jedis.config.Logger;
import com.example.spring.jedis.entity.User;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

/**
 * 这里简单介绍一下Spring的三个缓存注解：@CachePut、@CacheEvict、@Cacheable。
 * 这三个注解通常都加在方法的前面，大致的作用如下：
 * （1）@CachePut作用是设置缓存。先执行方法，并将执行结果缓存起来。
 * （2）@CacheEvict的作用是删除缓存。在执行方法后，删除缓存。也可以配置成为执
 * 行后删除，可以通过其beforeInvocation 这个参数配置，其默认值为false。
 * （3）@Cacheable的作用更多是查询缓存。首先检查注解中的Key键是否在缓存中，如
 * 果是，则返回Key的缓存值，不再执行方法；否则，执行方法并将方法结果缓存起来。从
 * 后半部分来看，@Cacheable也具备@CachePut的能力。
 *
 * 详解@CachePut和@Cacheable注解
 * 简单来说，这两个注解都可以增加缓存，但是有细微的区别：
 * （1）@CachePut负责增加缓存。
 * （2）@Cacheable负责查询缓存，如果没有查到， 才去执行被注解的方法，并将方法
 * 的结果增加到缓存
 */
@Service
@CacheConfig(cacheNames = "userCache")
public class UserServiceImplWithAnno implements UserService {

    public static final String USER_UID_PREFIX = "'userCache:'+";

    /**
     * CRUD 之  新增/更新
     *
     * @param user 用户
     */
    /**
     *@CachePut 注解
     * （1）value属性，指定Cache缓存的名称
     * （2）key属性：指定Redis的Key属性值
     * key属性，是用来指定Spring缓存方法的Key键，该属性支持SpringEL表达式。当没有
     * 指定该属性时，Spring将使用默认策略生成Key键。有关SpringEL表达式，稍候再详细介
     * 绍。
     * （3）condition属性：指定缓存的条件
     * 并不是所有的函数结果都希望加入Redis缓存，可以通过condition属性来实现这一功
     * 能。condition属性值默认为空，表示将缓存所有的结果。可以通过SpringEL表达式来设
     * 置，当表达式的值为true时，表示进行缓存处理；否则不进行缓存处理。如下示例程序表
     * 示只有当user的id大于1000时，才会进行缓存，
     *
     * @Cacheable 注解
     * Cacheable注解主要是查询缓存。对于加上了@Cacheable注解的方法，Spring在每次执
     * 行前都会检查Redis缓存中是否存在相同Key键，如果存在，就不再执行该方法，而是直接
     * 从缓存中获取结果并返回。如果不存在，才会执行方法，并将返回结果存入Redis缓存中。
     * 与@CachePut注解一样，@Cacheable也具备增加缓存的能力。
     * @Cacheable与@CachePut不同之处的是：@Cacheable只有当Key键在Redis缓存不存在
     * 的时候，才执行方法，将方法的结果缓存起来；如果Key键在Redis缓存中存在，则直接返
     * 回缓存结果。而加了@CachePut注解的方法，则缺少了检查的环节：@CachePut在方法执行
     * 前不去进行缓存检查，无论之前是否有缓存，都会将新的执行结果加入到缓存中。
     */
    @CachePut(key = USER_UID_PREFIX + "T(String).valueOf(#user.uid)")
    @Override
    public User saveUser(final User user) {
        //保存到数据库
        //返回值，将保存到缓存
        Logger.info("user : save to redis");
        return user;
    }

    /**
     * 带条件缓存
     *
     * @param user 用户
     * @return 用户
     */
    @CachePut(key = "T(String).valueOf(#user.uid)", condition = "#user.uid>1000")
    public User cacheUserWithCondition(final User user) {
        //保存到数据库
        //返回值，将保存到缓存
        Logger.info("user : save to redis");
        return user;
    }


    /**
     * CRUD 之   查询
     *
     * @param id id
     * @return 用户
     */
    @Cacheable(key = USER_UID_PREFIX + "T(String).valueOf(#id)")
    @Override
    public User getUser(final long id) {
        //如果缓存没有,则从数据库中加载
        Logger.info("user : is null");
        return null;
    }

    /**
     * CRUD 之 删除
     *
     * @param id id
     */

    @CacheEvict(key = USER_UID_PREFIX + "T(String).valueOf(#id)")
    @Override
    public void deleteUser(long id) {

        //从数据库中删除
        Logger.info("delete  User:", id);
    }

    /**
     * 删除userCache中的全部缓存
     */
    @CacheEvict(value = "userCache", allEntries = true)
    public void deleteAll() {

    }


    /**
     * 一个方法上，加上三类cache处理
     */
    @Caching(cacheable = @Cacheable(key = "'userCache:'+ #uid"),
            put = @CachePut(key = "'userCache:'+ #uid"),
            evict = {
                    @CacheEvict(key = "'userCache:'+ #uid"),
                    @CacheEvict(key = "'addressCache:'+ #uid"),
                    @CacheEvict(key = "'messageCache:'+ #uid")
            }
    )
    public User updateRef(String uid) {
        //....业务逻辑
        return null;
    }


}
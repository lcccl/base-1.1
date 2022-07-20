package base.framework.distributed.impl.redis;

import base.framework.distributed.DistributedFactory;
import base.framework.distributed.Hash;
import base.framework.distributed.Lock;
import base.framework.distributed.Queue;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

/**
 * Created by cl on 2018/4/9.
 * 基于redis实现的分布式组件工厂
 */
public class RedisDistributedFactory implements DistributedFactory {

    private JedisExecutor jedisExecutor;

    public RedisDistributedFactory(JedisPool jedisPool) {
        this.jedisExecutor = JedisExecutorProxy.createProxy(jedisPool);
    }

    @Override
    public Lock createLock(Serializable id) {
        return new RedisLock(jedisExecutor, id.toString());
    }

    @Override
    public <T> Queue<T> createQueue(Serializable id, Class<T> clazz) {
        return new RedisQueue<T>(jedisExecutor, id.toString(), clazz);
    }

    @Override
    public <T> Hash<T> createHash(Serializable id, Class<T> clazz) {
        return new RedisHash<T>(jedisExecutor, id.toString(), clazz);
    }

}

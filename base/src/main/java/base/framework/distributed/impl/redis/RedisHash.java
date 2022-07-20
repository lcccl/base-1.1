package base.framework.distributed.impl.redis;

import base.framework.distributed.DistributedException;
import base.framework.distributed.Hash;
import base.utils.serializer.Serializer;
import base.utils.serializer.impl.JsonSerializer;

import java.util.Set;

/**
 * Created by cl on 2018/7/17.
 * 基于Redis实现的分布式Hash表
 */
public class RedisHash<T> implements Hash<T> {

    private final static Serializer serializer = new JsonSerializer();

    private JedisExecutor jedisExecutor;

    private String hashId;

    private Class<T> clazz;

    public RedisHash(JedisExecutor jedisExecutor, String hashId, Class<T> clazz) {
        this.jedisExecutor = jedisExecutor;
        this.hashId = hashId;
        this.clazz = clazz;
    }

    @Override
    public void put(String key, T value) {
        try {
            String str = new String(serializer.serialize(value), "UTF-8");
            jedisExecutor.hset(hashId, key, str);
        } catch (Exception e) {
            throw new DistributedException("failed to put [key: " + key + ", value: " +
                    value + "], reason: " + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public T get(String key) {
        String str = jedisExecutor.hget(hashId, key);
        if (null == str || str.trim().length() == 0) {
            return null;
        }

        try {
            return (T) serializer.deserialize(str.getBytes("UTF-8"), clazz);
        } catch (Exception e) {
            throw new DistributedException("failed to get key: " + key + ", reason: " +
                    e.getLocalizedMessage(), e);
        }
    }

    @Override
    public T remove(String key) {
        T val = this.get(key);
        if (null != val) {
            jedisExecutor.hdel(hashId, key);
        }
        return val;
    }

    @Override
    public int size() {
        return jedisExecutor.hlen(hashId).intValue();
    }

    @Override
    public Set<String> keys() {
        return jedisExecutor.hkeys(hashId);
    }

    @Override
    public void clear() {
        jedisExecutor.del(hashId);
    }

}

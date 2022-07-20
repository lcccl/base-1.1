package base.framework.cache.impl;

import base.framework.cache.CacheService;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * Created by cl on 2017/4/20.
 * Redis缓存服务
 */
public class RedisCacheServiceImpl implements CacheService {

    private final static String CONNECTOR = "_#_";

    private RedisTemplate template;

    @Override
    public Object get(String cacheName, String key) {
        BoundValueOperations<String, Object> data = this.getCache(cacheName, key);
        return data.get();
    }

    @Override
    public void put(String cacheName, String key, Object value) {
        BoundValueOperations<String, Object> data = this.getCache(cacheName, key);
        data.set(value);
    }

    @Override
    public void clear(String cacheName) {
        Set<String> keys = template.keys(cacheName + CONNECTOR + "*");
        template.delete(keys);
    }

    private BoundValueOperations<String, Object> getCache(String cacheName, String key) {
        String cacheKey = cacheName + CONNECTOR + key;
        return template.boundValueOps(cacheKey);
    }

    /*=========================== Getters and Setters ===========================*/
    public void setTemplate(RedisTemplate template) {
        this.template = template;
    }

}

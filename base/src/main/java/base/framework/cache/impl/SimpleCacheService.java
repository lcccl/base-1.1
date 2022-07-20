package base.framework.cache.impl;

import base.framework.cache.CacheService;
import base.framework.cache.simple.SimpleCache;
import base.framework.cache.simple.SimpleLRUCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/8/14.
 * 简单的缓存服务，采用LRU算法的简单缓存实现
 */
public class SimpleCacheService implements CacheService {

    private Map<String, SimpleCache> cacheMap = new HashMap<String, SimpleCache>();

    @Override
    public Object get(String cacheName, String key) {
        return this.getCache(cacheName).get(key);
    }

    @Override
    public void put(String cacheName, String key, Object value) {
        this.getCache(cacheName).put(key, value);
    }

    @Override
    public void clear(String cacheName) {
        this.getCache(cacheName).removeAll();
    }

    private SimpleCache getCache(String cacheName) {
        SimpleCache cache = cacheMap.get(cacheName);
        if (null == cache) {
            synchronized (this) {
                cache = cacheMap.get(cacheName);
                if (null == cache) {
                    cache = new SimpleLRUCache();
                    cacheMap.put(cacheName, cache);
                }
            }
        }

        return cache;
    }

}

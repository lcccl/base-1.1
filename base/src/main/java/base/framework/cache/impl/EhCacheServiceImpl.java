package base.framework.cache.impl;

import base.framework.cache.CacheService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by cl on 2017/4/20.
 * EhCache缓存服务
 */
public class EhCacheServiceImpl implements CacheService {

    private Log logger = LogFactory.getLog(EhCacheServiceImpl.class);

    private CacheManager cacheManager;

    @Override
    public Object get(String cacheName, String key) {
        Cache cache = this.getCache(cacheName);
        Element element = cache.get(key);
        return null != element ? element.getObjectValue() : null;
    }

    @Override
    public void put(String cacheName, String key, Object value) {
        Cache cache = this.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    @Override
    public void clear(String cacheName) {
        Cache cache = this.getCache(cacheName);
        cache.removeAll();
    }

    private Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            logger.error("找不到缓存：" + cacheName);

            synchronized (this) {
                cache = cacheManager.getCache(cacheName);
                if (null == cache) {
                    cache = new Cache(cacheName, 10000, false, false, 600, 600);
                    cacheManager.addCache(cache);
                    logger.info("创建缓存：" + cacheName);
                }
            }
        }

        return cache;
    }

    /*=========================== Getters and Setters ===========================*/
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}

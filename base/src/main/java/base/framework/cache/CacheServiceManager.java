package base.framework.cache;

import base.framework.cache.impl.SimpleCacheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cl on 2017/5/10.
 * 缓存服务管理器
 */
public class CacheServiceManager {

    private final static Log LOGGER = LogFactory.getLog(CacheServiceManager.class);

    /* 单例 */
    private static CacheServiceManager instance;

    /* 缓存服务容器 */
    private Map<String, CacheService> cacheServiceMap = new HashMap<String, CacheService>();

    /* 默认的缓存类型 */
    private String defaultCacheType;

    private CacheServiceManager() {
        // 注册系统自带的SimpleCacheService
        this.registerCacheService("simpleCache", new SimpleCacheService());
    }

    /**
     * 获取缓存服务
     */
    public CacheService getCacheService(String cacheType) {
        return cacheServiceMap.get(cacheType);
    }

    /**
     * 获取默认的缓存服务
     */
    public CacheService getCacheService() {
        CacheService cacheService = this.getCacheService(defaultCacheType);
        // 默认的缓存服务不存在时，取缓存服务容器中的第一个
        if (null == cacheService) {
            Iterator<CacheService> it = cacheServiceMap.values().iterator();
            if (it.hasNext()) {
                cacheService = it.next();
            }
        }

        return cacheService;
    }

    /**
     * 注册缓存服务
     */
    public void registerCacheService(String cacheType, CacheService cacheService) {
        cacheServiceMap.put(cacheType, cacheService);
        LOGGER.info("CacheService registered, cacheType:" + cacheType + ", instance:" + cacheService);
    }


    /**
     * 设置默认的缓存类型
     */
    public void setDefaultCacheType(String cacheType) {
        this.defaultCacheType = cacheType;
    }

    public static CacheServiceManager getInstance() {
        if (null == instance) {
            synchronized (CacheServiceManager.class) {
                if (null == instance) {
                    instance = new CacheServiceManager();
                }
            }
        }
        return instance;
    }

}

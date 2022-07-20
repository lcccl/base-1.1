package base.framework.cache;

/**
 * Created by cl on 2017/4/20.
 * 缓存服务接口
 */
public interface CacheService {

    /**
     * 从缓存中获取数据
     */
    Object get(String cacheName, String key);

    /**
     * 将数据放入缓存中
     */
    void put(String cacheName, String key, Object value);

    /**
     * 清空缓存
     */
    void clear(String cacheName);

}

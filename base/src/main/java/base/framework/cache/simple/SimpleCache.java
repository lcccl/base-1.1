package base.framework.cache.simple;

/**
 * Created by cl on 2017/8/15.
 * 简单的缓存
 */
public interface SimpleCache {

    Object get(Object key);

    void put(Object key, Object val);

    Object remove(Object key);

    void removeAll();

}

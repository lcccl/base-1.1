package base.framework.distributed;

import java.util.Set;

/**
 * Created by cl on 2018/5/9.
 * 分布式Hash表
 */
public interface Hash<T> {

    void put(String key, T value);

    T get(String key);

    T remove(String key);

    int size();

    Set<String> keys();

    void clear();

}

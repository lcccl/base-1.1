package base.framework.distributed;

import java.io.Serializable;

/**
 * Created by cl on 2018/4/9.
 * 分布式组件工厂
 */
public interface DistributedFactory {

    /**
     * 创建分布式锁
     *
     * @param id ID标识
     * @return
     */
    Lock createLock(Serializable id);

    /**
     * 创建分布式队列
     *
     * @param id    ID标识
     * @param clazz 队列中元素的类型
     * @param <T>   泛型类型
     * @return
     */
    <T> Queue<T> createQueue(Serializable id, Class<T> clazz);

    /**
     * 创建Hash表
     *
     * @param id    ID标识
     * @param clazz Hash表中元素的类型
     * @param <T>   泛型类型
     * @return
     */
    <T> Hash<T> createHash(Serializable id, Class<T> clazz);

}

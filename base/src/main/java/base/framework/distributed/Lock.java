package base.framework.distributed;

/**
 * Created by cl on 2018/4/8.
 * 分布式锁
 */
public interface Lock {

    /**
     * 加锁
     *
     * @param timeout    等待获取锁的超时时间(ms)
     * @param expireTime 锁的过期时间(ms)
     */
    void lock(int timeout, int expireTime);

    /**
     * 解锁
     */
    void unlock();

}

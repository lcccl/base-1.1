package base.framework.distributed.impl.redis;

import base.framework.distributed.DistributedException;
import base.framework.distributed.Lock;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by cl on 2018/4/9.
 * 基于redis实现的分布式锁
 */
public class RedisLock implements Lock {

    private final static String LOCK_SUCCESS = "OK";
    private final static String SET_IF_NOT_EXIST = "NX";
    private final static String EXPIRE_TIME_UNIT = "PX";
    private final static Long RELEASE_SUCCESS = 1L;

    private JedisExecutor jedisExecutor;

    private String lockId;

    private String instanceId;

    public RedisLock(JedisExecutor jedisExecutor, String lockId) {
        this.jedisExecutor = jedisExecutor;
        this.lockId = lockId;
        instanceId = UUID.randomUUID().toString();
    }

    /**
     * 加锁
     * <p>
     * 通过redis的set方法获取锁，lockId是锁的唯一标识；
     * instanceId为锁的持有者的标识，解锁时只有锁的持有者才能解锁；
     * 只有当key为lockId的元素不存在时，才能set成功，否则不做任何操作，保证只有一个实例能持有锁；
     * 给set的数据加一个过期时间，防止程序出错，锁一直被占用；
     * 锁被占用时，每隔一段时间再次尝试获取锁，超时后抛出异常
     */
    @Override
    public void lock(int timeout, int expireTime) {
        int intervalTime = timeout < 10 ? Math.max(2, timeout / 3) : 10;

        long startTime = System.currentTimeMillis();

        while (true) {
            String result = jedisExecutor.set(lockId, instanceId,
                    SET_IF_NOT_EXIST, EXPIRE_TIME_UNIT, expireTime);

            if (LOCK_SUCCESS.equals(result)) {
                break;
            }

            long nowTime = System.currentTimeMillis();
            if (nowTime - startTime > timeout) {
                throw new DistributedException("timeout to get lock:" + this.toString());
            }

            try {
                Thread.currentThread().sleep(intervalTime);
            } catch (InterruptedException e) {
                throw new DistributedException(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public void unlock() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        Object result = jedisExecutor.eval(script, Collections.singletonList(lockId), Collections.singletonList(instanceId));

        if (!RELEASE_SUCCESS.equals(result)) {
            throw new DistributedException("failed to unlock:" + lockId);
        }
    }

    @Override
    public String toString() {
        return "[lockId: " + lockId + ", instanceId:" + instanceId + "]";
    }

}

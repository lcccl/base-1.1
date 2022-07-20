package base.framework.distributed.impl.redis;

import base.framework.distributed.DistributedException;
import base.framework.distributed.Queue;
import base.utils.serializer.Serializer;
import base.utils.serializer.impl.JsonSerializer;

/**
 * Created by cl on 2018/4/9.
 * 基于Redis实现的分布式队列
 */
public class RedisQueue<T> implements Queue<T> {

    private final static String EMPTY_VALUE = "nil";

    private final static Serializer serializer = new JsonSerializer();

    private JedisExecutor jedisExecutor;

    private String queueId;

    private Class<T> clazz;

    public RedisQueue(JedisExecutor jedisExecutor, String queueId, Class<T> clazz) {
        this.jedisExecutor = jedisExecutor;
        this.queueId = queueId;
        this.clazz = clazz;
    }

    @Override
    public void push(T o) {
        try {
            String str = new String(serializer.serialize(o), "UTF-8");
            jedisExecutor.lpush(queueId, str);
        } catch (Exception e) {
            throw new DistributedException("failed to push:" + o.toString() + ", reason:\n" +
                    e.getLocalizedMessage(), e);
        }
    }

    @Override
    public T pop() {
        String value = jedisExecutor.rpop(queueId);
        if (null == value || EMPTY_VALUE.equals(value)) {
            return null;
        }

        try {
            return (T) serializer.deserialize(value.getBytes("UTF-8"), clazz);
        } catch (Exception e) {
            // 异常时重新放回redis队列中
            jedisExecutor.lpush(queueId, value);
            throw new DistributedException("failed to pop, reason:\n" + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public int size() {
        return jedisExecutor.llen(queueId).intValue();
    }

    @Override
    public void clear() {
        jedisExecutor.del(queueId);
    }

}

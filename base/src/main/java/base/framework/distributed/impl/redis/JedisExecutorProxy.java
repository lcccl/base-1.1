package base.framework.distributed.impl.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by cl on 2018/4/9.
 * JedisExecutor动态代理
 */
public class JedisExecutorProxy implements InvocationHandler {

    private JedisPool jedisPool;

    private JedisExecutorProxy(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return method.invoke(jedis, args);
        } finally {
            // 使用完后关闭连接
            String methodName = method.getName();
            if (!methodName.equals("close") && null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     * 创建JedisExecutor接口的动态代理
     *
     * @param jedisPool jedis连接池
     * @return
     */
    public static JedisExecutor createProxy(JedisPool jedisPool) {
        JedisExecutorProxy handler = new JedisExecutorProxy(jedisPool);
        return (JedisExecutor) Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                new Class<?>[]{JedisExecutor.class}, handler);
    }

}

package base.config;

import base.framework.cache.CacheService;
import base.framework.cache.CacheServiceManager;
import base.framework.cache.impl.RedisCacheServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by cl on 2017/4/20.
 * Redis缓存配置
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean(name = "redisCacheService")
    public CacheService cacheService() {
        // 初始化Redis
        RedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer valueSerializer = new JdkSerializationRedisSerializer();

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        // 创建Redis缓存服务
        RedisCacheServiceImpl cacheService = new RedisCacheServiceImpl();
        cacheService.setTemplate(redisTemplate);

        // 注册到缓存服务管理器
        CacheServiceManager.getInstance().registerCacheService("redis", cacheService);

        return cacheService;
    }

}

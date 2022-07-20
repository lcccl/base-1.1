package base.config;

import base.framework.cache.CacheServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by cl on 2017/5/10.
 * 缓存服务配置
 */
@Configuration
public class CacheServiceConfig {

    @Autowired
    private Environment env;

    @Bean
    public CacheServiceManager cacheServiceManager() {
        CacheServiceManager manager = CacheServiceManager.getInstance();
        // 根据配置文件设置默认的缓存类型
        String cacheType = env.getProperty("cache.cacheType");
        if (null != cacheType) {
            manager.setDefaultCacheType(cacheType);
        }

        return manager;
    }

}

package base.config;

import base.framework.cache.CacheService;
import base.framework.cache.CacheServiceManager;
import base.framework.cache.impl.EhCacheServiceImpl;
import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * Created by cl on 2017/4/27.
 * EhCache缓存配置
 */
@Configuration
@EnableCaching
public class EhCacheConfig {

    @Bean(name = "ehCacheManager")
    public CacheManager ehCacheManager() {
        // 初始化EhCache，ehcache.xml放在classpath根目录，找不到时加载默认目录下的配置文件
        InputStream in = this.getClass().getResourceAsStream("/ehcache.xml");
        if (null == in) {
            in = this.getClass().getResourceAsStream("/base/framework/cache/ehcache.xml");
        }
        return CacheManager.newInstance(in);
    }

    @Bean(name = "ehCacheService")
    public CacheService cacheService() {
        // 创建EhCache缓存服务
        EhCacheServiceImpl cacheService = new EhCacheServiceImpl();
        cacheService.setCacheManager(ehCacheManager());

        // 注册到缓存服务管理器
        CacheServiceManager.getInstance().registerCacheService("ehcache", cacheService);

        return cacheService;
    }

}

package base.admin.config;

import base.admin.model.po.User;
import base.admin.security.ShiroUserRealm;
import base.admin.service.UserService;
import base.config.*;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by cl on 2017/11/17.
 * 系统配置类，通过注解导入各个功能组件的配置
 */
@Configuration
@Import({
        JpaDaoConfig.class,
        RedisCacheConfig.class,
        EhCacheConfig.class,
        CacheServiceConfig.class,
        PacketTransferServiceConfig.class,
        ExtMvcAutoConfig.class,
        SysUtilsConfig.class,
        CorsFilterConfig.class,
        ShiroConfig.class
})
public class SystemConfig implements ApplicationContextAware {

    @Autowired
    private UserService userService;

    /**
     * shiro的权限认证realm
     */
    @Bean
    public AuthorizingRealm shiroRealm() {
        ShiroUserRealm realm = new ShiroUserRealm();
        realm.setUserService(userService);
        return realm;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化，创建系统管理员
        User adminUser = userService.findByUserName("admin");
        if (null == adminUser) {
            adminUser = new User();
            adminUser.setUserName("admin");
            adminUser.setPassword("admin");
            adminUser.setName("系统管理员");

            userService.saveUser(adminUser);
        }
    }

}

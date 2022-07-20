package base.config;

import base.framework.security.shiro.*;
import base.framework.security.shiro.definition.DbFilterChainDefMapBean;
import base.framework.security.shiro.definition.DefaultFilterChainDefMapBean;
import base.utils.CommonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.InputStream;

/**
 * Created by cl on 2017/10/26.
 * shiro配置
 */
@Configuration
public class ShiroConfig {

    private final static Log logger = LogFactory.getLog(ShiroConfig.class);

    @Autowired
    private AuthorizingRealm realm;

    @Autowired(required = false)
    private CredentialsMatcher credentialsMatcher;

    @Autowired(required = false)
    private FilterChainDefinitionMapBean definitionMapBean;

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(ShiroConfiguration cfg) {
        try {
            ExtShiroFilterFactoryBean factoryBean = new ExtShiroFilterFactoryBean(cfg);
            factoryBean.createDefaultSecurityManager(realm, credentialsMatcher);
            return factoryBean;
        } catch (Exception e) {
            logger.error("ShiroFilterFactoryBean初始化失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 密码加密器，用于创建用户时加密密码
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncryptor.class)
    public PasswordEncryptor passwordEncryptor(ShiroConfiguration cfg) {
        ShiroConfiguration.PasswordEncryption pe = cfg.getPasswordEncryption();
        return new DefaultPasswordEncryptor(pe.algorithmName, pe.hashIterations, pe.toHex);
    }

    @Bean
    public ShiroConfiguration shiroConfiguration(DataSource dataSource) {
        try {
            ShiroConfiguration.registerDefinitionMapBean("default", new DefaultFilterChainDefMapBean());
            ShiroConfiguration.registerDefinitionMapBean("db", new DbFilterChainDefMapBean(dataSource));
            ShiroConfiguration.registerDefinitionMapBean("custom", definitionMapBean);

            InputStream in = CommonUtils.getClassPathResourceAsStream("shiro-config.xml");
            return ShiroConfiguration.load(in);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new RuntimeException("shiro-config.xml配置文件加载失败");
        }
    }

}

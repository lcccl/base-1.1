package base.framework.security.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cl on 2017/10/27.
 * ShiroFilter初始化工厂bean
 */
public class ExtShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    protected ShiroConfiguration cfg;

    public ExtShiroFilterFactoryBean(ShiroConfiguration cfg) throws Exception {
        this.cfg = cfg;

        // 设置shiro的过滤器链
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        for (Map.Entry<String, String> entry : cfg.getFilterChain().entrySet()) {
            String filterName = entry.getKey();
            String filterClass = entry.getValue();
            filters.put(filterName, (Filter) Class.forName(filterClass).newInstance());
        }
        this.setFilters(filters);

        // 过滤链规则配置
        this.setFilterChainDefinitionMap(cfg.getFilterChainDefinitionMap());

        // 设置登录页面
        this.setLoginUrl(cfg.getLoginUrl());
        // 设置登录成功后的跳转页面
        this.setSuccessUrl(cfg.getSuccessUrl());
        // 设置未授权跳转页面
        this.setUnauthorizedUrl(cfg.getUnauthorizedUrl());
    }

    /**
     * 创建默认的SecurityManager
     */
    public void createDefaultSecurityManager(AuthorizingRealm realm, CredentialsMatcher credentialsMatcher) {
        if (null == credentialsMatcher) {
            credentialsMatcher = this.createDefaultCredentialsMatcher();
        }

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        realm.setCredentialsMatcher(credentialsMatcher);
        securityManager.setRealm(realm);
        this.setSecurityManager(securityManager);
    }

    /**
     * 创建默认的凭证匹配器CredentialsMatcher
     */
    protected CredentialsMatcher createDefaultCredentialsMatcher() {
        ShiroConfiguration.PasswordEncryption pe = cfg.getPasswordEncryption();

        // 根据密码加密策略配置创建默认的凭证匹配器
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(pe.algorithmName);
        matcher.setHashIterations(pe.hashIterations);
        matcher.setStoredCredentialsHexEncoded(pe.toHex);

        return matcher;
    }

}

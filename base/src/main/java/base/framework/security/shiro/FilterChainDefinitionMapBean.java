package base.framework.security.shiro;


import java.util.Map;

/**
 * Created by cl on 2017/12/10.
 * 过滤链映射Bean
 */
public interface FilterChainDefinitionMapBean {

    /**
     * 创建过滤链映射
     */
    Map<String, String> createDefinitionMap(Map<String, String> attrs, String text);

}

package base.framework.security.shiro;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cl on 2017/10/26.
 * 简化的shiro配置
 */
public class ShiroConfiguration {

    /* 过滤器链 */
    private Map<String, String> filterChain = new LinkedHashMap<String, String>();
    /* 过滤链规则配置 */
    private Map<String, String> filterChainDefinitionMap;
    /* 登录页面 */
    private String loginUrl;
    /* 登录成功后的跳转页面 */
    private String successUrl;
    /* 未授权跳转页面 */
    private String unauthorizedUrl;
    /* 密码加密策略 */
    private PasswordEncryption passwordEncryption = new PasswordEncryption();

    /* FilterChainDefinitionMapBean容器 */
    private static Map<String, FilterChainDefinitionMapBean> definitionMapBeans = new HashMap<String, FilterChainDefinitionMapBean>();

    private ShiroConfiguration() {
    }

    /**
     * 加载shiro配置文件
     */
    public static ShiroConfiguration load(InputStream in) throws Exception {
        ShiroConfiguration config = new ShiroConfiguration();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            config.init(doc);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return config;
    }

    /**
     * 加载shiro配置文件
     */
    public static ShiroConfiguration load(String text) throws Exception {
        ShiroConfiguration config = new ShiroConfiguration();
        Document doc = DocumentHelper.parseText(text);
        config.init(doc);
        return config;
    }

    /**
     * 注册FilterChainDefinitionMapBean
     */
    public static void registerDefinitionMapBean(String type, FilterChainDefinitionMapBean definitionMapBean) {
        definitionMapBeans.put(type, definitionMapBean);
    }

    /**
     * 初始化
     */
    private void init(Document doc) {
        // 过滤器链
        Node filterNode = doc.selectSingleNode("/shiro/filterChain");
        if (null != filterNode) {
            for (Object o : filterNode.selectNodes("filter")) {
                Element e = (Element) o;
                filterChain.put(e.attributeValue("name"), e.attributeValue("class"));
            }
        }

        // 过滤链规则配置
        Element node = (Element) doc.selectSingleNode("/shiro/filterChainDefinition");
        Map<String, String> attrs = new HashMap<String, String>();
        for (Object o : node.attributes()) {
            Attribute attr = (Attribute) o;
            attrs.put(attr.getName(), attr.getValue());
        }
        String type = StringUtils.isEmpty(attrs.get("type")) ? "default" : attrs.get("type"),
                text = node.getText();
        FilterChainDefinitionMapBean definitionMapBean = definitionMapBeans.get(type);
        filterChainDefinitionMap = definitionMapBean.createDefinitionMap(attrs, text);

        // 设置url
        loginUrl = doc.selectSingleNode("/shiro/loginUrl").getText();
        successUrl = doc.selectSingleNode("/shiro/successUrl").getText();
        unauthorizedUrl = doc.selectSingleNode("/shiro/unauthorizedUrl").getText();

        // 密码加密策略
        Element el = (Element) doc.selectSingleNode("/shiro/passwordEncryption");
        passwordEncryption.algorithmName = el.selectSingleNode("algorithmName").getText();
        passwordEncryption.hashIterations = Integer.parseInt(el.selectSingleNode("hashIterations").getText());
        passwordEncryption.toHex = Boolean.valueOf(el.selectSingleNode("toHex").getText());
    }

    public Map<String, String> getFilterChain() {
        return filterChain;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public PasswordEncryption getPasswordEncryption() {
        return passwordEncryption;
    }

    /**
     * 密码加密策略
     */
    public static class PasswordEncryption {
        /* 散列算法 */
        public String algorithmName;
        /* 散列次数 */
        public int hashIterations;
        /* 加密的编码 */
        public boolean toHex;
    }

}

package base.config;

import base.framework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by cl on 2017/9/21.
 * 跨域资源共享Filter初始化配置
 */
@Configuration
public class CorsFilterConfig {

    @Autowired
    private CorsFilterProperties filterProperties;

    /**
     * properties配置文件中存在cors.filter.name时才创建Filter
     */
    @Bean
    @ConditionalOnProperty(prefix = "cors.filter", name = "name")
    public FilterRegistrationBean corsFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(new CorsFilter());
        registration.setName(filterProperties.getName());
        registration.setInitParameters(filterProperties.getInitParams());
        registration.addUrlPatterns(filterProperties.getUrlPatterns().split(","));
        registration.setOrder(filterProperties.getOrder());

        return registration;
    }

    /**
     * CorsFilter配置
     */
    @Component
    @ConfigurationProperties(prefix = "cors.filter")
    public static class CorsFilterProperties {
        /* filter-name */
        private String name;

        /* init-param 初始化参数 */
        private Map<String, String> initParams;

        private String urlPatterns;

        private int order;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getInitParams() {
            return initParams;
        }

        public void setInitParams(Map<String, String> initParams) {
            this.initParams = initParams;
        }

        public String getUrlPatterns() {
            return urlPatterns;
        }

        public void setUrlPatterns(String urlPatterns) {
            this.urlPatterns = urlPatterns;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

    }

}

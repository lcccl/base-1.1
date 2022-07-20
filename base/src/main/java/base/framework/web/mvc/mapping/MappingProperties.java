package base.framework.web.mvc.mapping;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by cl on 2017/7/24.
 * 映射策略配置
 */
@Component
@ConfigurationProperties(prefix = "mvc.mapping")
public class MappingProperties {

    /* 全局策略 */
    private String strategy;

    /* 扩展的请求映射创建器配置 */
    private Map<String, String> creators;

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Map<String, String> getCreators() {
        return creators;
    }

    public void setCreators(Map<String, String> creators) {
        this.creators = creators;
    }

}

package base.framework.security.shiro.definition;

import base.framework.security.shiro.FilterChainDefinitionMapBean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2017/12/10.
 * 读取数据库配置的FilterChainDefinitionMapBean实现
 */
public class DbFilterChainDefMapBean implements FilterChainDefinitionMapBean {

    private JdbcTemplate jdbcTemplate;

    public DbFilterChainDefMapBean(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Map<String, String> createDefinitionMap(Map<String, String> attrs, String text) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(text.trim());
        if (null != list && list.size() > 0) {
            for (Map<String, Object> row : list) {
                String url = (String) row.get("url");
                String filter = (String) row.get("filter");
                map.put(url, filter);
            }
        }

        return map;
    }

}

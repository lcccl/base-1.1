package base.framework.dao.sql.template;

import java.util.Map;

/**
 * Created by cl on 2017/6/7.
 * SQL模板引擎
 */
public interface SqlTemplateEngine {

    /**
     * 解析SQL模板
     */
    SqlResult make(SqlTemplate sqlTemplate, Map<String, Object> model);

    /**
     * 清理模板缓存
     */
    void clearCache();

}

package base.framework.dao.sql.template.engines;

import base.framework.dao.sql.template.SqlResult;
import base.framework.dao.sql.template.SqlTemplate;
import base.framework.dao.sql.template.SqlTemplateEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/6/7.
 * SQL模板预处理的模板引擎，供子类继承
 */
public abstract class PreparedSqlTemplateEngine<T> implements SqlTemplateEngine {

    protected Log logger = LogFactory.getLog(PreparedSqlTemplateEngine.class);

    /* 模板对象缓存容器 */
    private Map<String, T> templateCache = new HashMap<String, T>();

    @Override
    public SqlResult make(SqlTemplate sqlTemplate, Map<String, Object> model) {
        String id = sqlTemplate.getId();
        T template = templateCache.get(id);

        if (null == template) {
            synchronized (templateCache) {
                template = templateCache.get(id);
                if (null == template) {
                    String tpl = sqlTemplate.getTpl();
                    logger.info("原始的SQL模板：" + tpl);
                    tpl = this.processTemplate(tpl);
                    logger.info("预处理后的SQL模板：" + tpl);

                    logger.info("创建模板对象");
                    try {
                        template = this.createTemplate(tpl);
                    } catch (Exception e) {
                        throw new RuntimeException("模板对象创建失败[" + tpl + "]\n" + e.getLocalizedMessage(), e);
                    }
                    templateCache.put(id, template);
                }
            }
        }

        try {
            return this.callTemplate(template, model);
        } catch (Exception e) {
            throw new RuntimeException("模板执行失败\n" + sqlTemplate.getTpl() + "\n" + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void clearCache() {
        templateCache.clear();
    }

    /**
     * 创建模板对象
     */
    protected abstract T createTemplate(String tpl) throws Exception;

    /**
     * 调用模板对象，解析SQL模板，生成SQL语句和参数
     */
    protected abstract SqlResult callTemplate(T template, Map<String, Object> model) throws Exception;

    /**
     * 处理模板，将模板中的#{xxx}中xxx取出替换成其他形式
     */
    protected String processTemplate(String tpl) {
        while (true) {
            int idx = tpl.indexOf("#{");
            if (idx == -1) {
                break;
            }

            for (int i = idx; i < tpl.length(); i++) {
                if (tpl.charAt(i) == '}') {
                    String param = tpl.substring(idx + 2, i).trim();
                    tpl = tpl.substring(0, idx) + this.replaceParam(param) + tpl.substring(i + 1);
                    break;
                }
            }
        }

        return tpl.trim();
    }

    /**
     * 替换#{xxx}部分
     */
    protected abstract String replaceParam(String param);

}

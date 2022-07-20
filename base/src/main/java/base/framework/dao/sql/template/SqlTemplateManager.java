package base.framework.dao.sql.template;

import base.framework.dao.sql.template.engines.FreeMarkerSqlTemplateEngine;
import base.framework.dao.sql.template.engines.GroovySqlTemplateEngine;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2017/6/5.
 * SQL模板管理器
 */
public class SqlTemplateManager {

    private final static Log logger = LogFactory.getLog(SqlTemplateManager.class);

    /* SQL模板引擎容器 */
    private Map<String, SqlTemplateEngine> engineMap = new HashMap<String, SqlTemplateEngine>();

    /* SQL配置文件路径 */
    private String sqlPath;

    /* 模板容器 */
    private Map<String, SqlTemplate> templateMap = new HashMap<String, SqlTemplate>();

    public SqlTemplateManager() {
        // 注册SQL模板引擎
        registerEngine("groovy", new GroovySqlTemplateEngine());
        registerEngine("freemarker", new FreeMarkerSqlTemplateEngine());
    }

    /**
     * 加载sql配置文件
     */
    public void load(String sqlPath) {
        this.sqlPath = sqlPath;

        load();
    }

    /**
     * 重新加载sql配置文件
     */
    public void reload() {
        for (SqlTemplateEngine engine : engineMap.values()) {
            engine.clearCache();
        }
        templateMap.clear();

        load();
    }

    /**
     * 根据sqlId获取sql模板处理结果
     */
    public SqlResult getSqlResult(String sqlId, Map<String, Object> params) {
        SqlTemplate template = templateMap.get(sqlId);
        if (null == template) {
            throw new RuntimeException("找不到sqlId：" + sqlId + "对应的sql配置");
        }

        // 调用对应的SQL模板引擎执行，创建SQL
        String type = template.getType();
        SqlTemplateEngine engine = engineMap.get(type);
        return engine.make(template, params);
    }

    /**
     * 注册SQL模板引擎
     */
    public void registerEngine(String type, SqlTemplateEngine engine) {
        engineMap.put(type, engine);
    }

    /**
     * 加载sql配置文件
     * 一般为classpath:xxx/xx/*.xml这样格式的路径；多模块多classpath时，格式如：classpath*:xxx/xx/*.xml；
     */
    private void load() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SAXReader saxReader = new SAXReader();
        // 待关闭的流列表
        List<InputStream> streamList = new ArrayList<InputStream>();

        try {
            Resource[] resources = resolver.getResources(sqlPath);
            for (Resource r : resources) {
                InputStream in = r.getInputStream();
                streamList.add(in);
                Document doc = saxReader.read(in);
                loadConfig(r, doc);
            }
        } catch (Exception e) {
            logger.error("加载" + sqlPath + "下的配置文件失败\n" + e.getLocalizedMessage(), e);
        } finally {
            // 关闭所有的流
            for (InputStream in : streamList) {
                IOUtils.closeQuietly(in);
            }
        }
    }

    /**
     * 加载sql模板配置，xml配置文件格式如下：
     * <sqls type="groovy">
     * <sql id="TestTab.query" type="freemarker">
     * <![CDATA[
     * select * from test_tab
     * ]]>
     * </sql>
     * </sqls>
     */
    private void loadConfig(Resource resource, Document doc) {
        Element root = doc.getRootElement();
        String defaultType = root.attributeValue("type");
        if (StringUtils.isBlank(defaultType)) {
            throw new RuntimeException(resource.getFilename() + "配置文件sqls节点type属性缺失");
        }

        List<?> list = root.elements();
        for (Object o : list) {
            Element e = (Element) o;
            String id = e.attributeValue("id");
            String type = e.attributeValue("type");
            String tpl = e.getText();

            SqlTemplate template = new SqlTemplate();
            template.setId(id);
            // 没有指定模板类型时使用默认类型
            template.setType(null != type ? type : defaultType);
            template.setTpl(tpl);

            templateMap.put(id, template);
        }
    }

}

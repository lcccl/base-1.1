package base.framework.dao.sql.template.engines;

import base.framework.dao.sql.template.ParamMap;
import base.framework.dao.sql.template.SqlResult;
import base.framework.dao.sql.template.SqlTemplateEngine;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/6/7.
 * 基于Groovy模板实现的SQL模板引擎
 */
public class GroovySqlTemplateEngine extends PreparedSqlTemplateEngine<Template> implements SqlTemplateEngine {

    private groovy.text.TemplateEngine engine = new GStringTemplateEngine();

    @Override
    protected Template createTemplate(String tpl) throws Exception {
        return engine.createTemplate(tpl);
    }

    @Override
    protected SqlResult callTemplate(Template template, Map<String, Object> model) throws Exception {
        // 构建模板执行上下文
        Map<String, Object> ctx = new HashMap<String, Object>();
        ParamMap paramMap = new ParamMap("param");
        ctx.put("paramMap", paramMap);
        ctx.putAll(model);

        StringWriter sw = new StringWriter();
        template.make(ctx).writeTo(sw);

        return new SqlResult(sw.toString(), paramMap);
    }

    /**
     * 重写processTemplate方法，为模板增加闭包
     */
    @Override
    protected String processTemplate(String tpl) {
        tpl = super.processTemplate(tpl);

        String closure = "";
        // 闭包buildParam：将参数转换成占位符
        closure += "<% def buildParam = { val, paramMap -> return paramMap.buildParam(val); }; %>";
        // 闭包forEach，迭代集合输出，每次输出间隔追加分隔符
        closure += "<% def forEach = { params -> " +
                "def data = params.data, " +
                "separator = params.separator?: '', " +
                "handler = params.handler," +
                "it = data instanceof Map ? data.entrySet().iterator() : data.iterator();" +
                "while (it.hasNext()) {" +
                "handler(it.next());" +
                "if (it.hasNext()) { out << separator;}" +
                "}" +
                "}; %>";

        return closure + "\n" + tpl;
    }

    /**
     * 将#{xxx}替换成${buildParam(xxx, paramMap)}的形式
     */
    @Override
    protected String replaceParam(String param) {
        return "${buildParam(" + param + ", paramMap)}";
    }

}

package base.framework.dao.sql.template.engines;

import base.framework.dao.sql.template.ParamMap;
import base.framework.dao.sql.template.SqlResult;
import base.framework.dao.sql.template.SqlTemplateEngine;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2017/6/7.
 * 基于FreeMarker模板实现的SQL模板引擎
 */
public class FreeMarkerSqlTemplateEngine extends PreparedSqlTemplateEngine<Template> implements SqlTemplateEngine {

    /* 模板的全局配置 */
    private Configuration globalConfig;

    public FreeMarkerSqlTemplateEngine() {
        globalConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        globalConfig.setSharedVariable("forEach", new ForEachDirective());
    }

    @Override
    protected Template createTemplate(String tpl) throws Exception {
        return new Template("FreeMarker_" + tpl.hashCode(), tpl, globalConfig);
    }

    @Override
    protected SqlResult callTemplate(Template template, Map<String, Object> model) throws Exception {
        // 将自定义模板函数加入上下文
        Map<String, Object> ctx = new HashMap<String, Object>();
        ParamMap paramMap = new ParamMap("param");
        ctx.put("buildParam", new BuildParamMethod(paramMap));
        ctx.putAll(model);

        StringWriter sw = new StringWriter();
        template.process(ctx, sw);

        return new SqlResult(sw.toString(), paramMap);
    }

    /**
     * 将#{xxx}替换成${buildParam(xxx)}
     */
    @Override
    protected String replaceParam(String param) {
        return "${buildParam(" + param + ")}";
    }

    /**
     * freemarker模板自定义函数：构建SQL参数占位符
     */
    private class BuildParamMethod implements TemplateMethodModelEx {

        private ParamMap paramMap;

        public BuildParamMethod(ParamMap paramMap) {
            this.paramMap = paramMap;
        }

        @Override
        public Object exec(List list) throws TemplateModelException {
            if (list.size() > 0) {
                Object val = list.get(0);
                if (val instanceof TemplateScalarModel) {
                    val = ((TemplateScalarModel) val).getAsString();
                } else if (val instanceof TemplateNumberModel) {
                    val = ((TemplateNumberModel) val).getAsNumber().doubleValue();
                }

                return paramMap.buildParam(val);
            }
            return null;
        }
    }

    /**
     * 自定义forEach指令/标签，迭代集合输出，每次输出间隔追加分隔符
     */
    private class ForEachDirective implements TemplateDirectiveModel {

        @Override
        public void execute(Environment env, Map params, TemplateModel[] loopVars,
                            TemplateDirectiveBody body) throws TemplateException, IOException {
            String item = ((TemplateScalarModel) params.get("item")).getAsString();
            String separator = params.containsKey("separator") ?
                    ((TemplateScalarModel) params.get("separator")).getAsString() : null;

            TemplateSequenceModel data = null;
            Object dataParam = params.get("data");
            // data为字符串时根据data字符串从env中取
            if (dataParam instanceof TemplateScalarModel) {
                dataParam = env.getVariable(((TemplateScalarModel) dataParam).getAsString());
            }

            if (dataParam instanceof TemplateSequenceModel) {
                data = (TemplateSequenceModel) dataParam;
            } else if (dataParam instanceof TemplateHashModel) {
                data = new SimpleSequence(((SimpleHash) dataParam).toMap().entrySet(),
                        new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
            }

            for (int i = 0; null != data && i < data.size(); i++) {
                TemplateModel obj = data.get(i);
                env.setVariable(item, obj);
                env.setVariable(item + "_index", new SimpleNumber(i));
                body.render(env.getOut());

                if (null != separator && i < data.size() - 1) {
                    env.getOut().append(separator);
                }
            }
        }
    }

}

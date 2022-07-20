package base.utils.template.engines;

import base.utils.template.TemplateEngine;
import freemarker.template.Template;

import java.io.Writer;
import java.util.Map;

/**
 * Created by cl on 2017/6/5.
 * FreeMarker实现的模板引擎
 */
public class FreeMarkerTemplateEngine extends AbstractTemplateEngine implements TemplateEngine {

    private final static String PREFIX = "FreeMarker_";

    @Override
    protected Object createTemplate(String tpl) throws Exception {
        return new Template(PREFIX + tpl.hashCode(), tpl, null);
    }

    @Override
    public void evaluate(String tpl, Map<String, Object> model, Writer writer, boolean cacheable) {
        try {
            Template template = (Template) this.createTemplate(tpl, cacheable);
            template.process(model, writer);
        } catch (Exception e) {
            throw new RuntimeException("模板执行失败\n" + tpl + "\n" + e.getLocalizedMessage(), e);
        }
    }

}

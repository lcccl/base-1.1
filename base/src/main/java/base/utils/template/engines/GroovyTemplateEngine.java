package base.utils.template.engines;

import base.utils.template.TemplateEngine;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

import java.io.Writer;
import java.util.Map;

/**
 * Created by cl on 2017/6/5.
 * Groovy实现的模板引擎
 */
public class GroovyTemplateEngine extends AbstractTemplateEngine implements TemplateEngine {

    private groovy.text.TemplateEngine engine = new GStringTemplateEngine();

    @Override
    protected Object createTemplate(String tpl) throws Exception {
        return engine.createTemplate(tpl);
    }

    @Override
    public void evaluate(String tpl, Map<String, Object> model, Writer writer, boolean cacheable) {
        try {
            Template template = (Template) this.createTemplate(tpl, cacheable);
            template.make(model).writeTo(writer);
        } catch (Exception e) {
            throw new RuntimeException("模板执行失败\n" + tpl + "\n" + e.getLocalizedMessage(), e);
        }
    }

}

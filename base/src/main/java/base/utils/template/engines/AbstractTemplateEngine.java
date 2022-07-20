package base.utils.template.engines;

import base.utils.template.TemplateEngine;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/6/5.
 * 模板引擎的抽象实现
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

    protected Log logger = LogFactory.getLog(AbstractTemplateEngine.class);

    /* 模板对象缓存 */
    protected Map<String, Object> templateCache = new HashMap<String, Object>();

    @Override
    public String evaluate(String tpl, Map<String, Object> model) {
        return this.evaluate(tpl, model, false);
    }

    @Override
    public void evaluate(Reader reader, Map<String, Object> model, Writer writer) {
        this.evaluate(reader, model, writer, false);
    }

    @Override
    public String evaluate(String tpl, Map<String, Object> model, boolean cacheable) {
        StringWriter sw = new StringWriter();
        this.evaluate(tpl, model, sw, cacheable);
        return sw.toString();
    }

    @Override
    public void evaluate(Reader reader, Map<String, Object> model, Writer writer, boolean cacheable) {
        String tpl = this.readTemplate(reader);
        this.evaluate(tpl, model, writer, cacheable);
    }

    @Override
    public void clearCache() {
        templateCache.clear();
    }

    /**
     * 通过输入流读取模板
     */
    protected String readTemplate(Reader reader) {
        StringWriter sw = new StringWriter();
        try {
            IOUtils.copy(reader, sw);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return sw.toString();
    }

    /**
     * 创建编译后模板的方法（带缓存）
     */
    protected Object createTemplate(String tpl, boolean cacheable) throws Exception {
        Object template = null;

        String key = this.getClass().toString() + tpl.hashCode();

        if (cacheable) {
            template = templateCache.get(key);
        }

        if (null == template) {
            template = this.createTemplate(tpl);

            if (cacheable) {
                templateCache.put(key, template);
            }
        }

        return template;
    }

    /**
     * 创建编译后的模板，由子类实现
     */
    protected abstract Object createTemplate(String tpl) throws Exception;

    /**
     * 执行模板，通过字符流输出字符串，由子类实现
     */
    protected abstract void evaluate(String tpl, Map<String, Object> model, Writer writer, boolean cacheable);

}

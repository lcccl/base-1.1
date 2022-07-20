package base.utils.template;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * Created by cl on 2017/6/5.
 * 模板引擎
 */
public interface TemplateEngine {

    /**
     * 执行模板，生成字符串（不缓存）
     */
    String evaluate(String tpl, Map<String, Object> model);

    /**
     * 执行模板，通过字符流读取模板并输出字符串（不缓存）
     */
    void evaluate(Reader reader, Map<String, Object> model, Writer writer);

    /**
     * 执行模板，生成字符串
     */
    String evaluate(String tpl, Map<String, Object> model, boolean cacheable);

    /**
     * 执行模板，通过字符流读取模板并输出字符串
     */
    void evaluate(Reader reader, Map<String, Object> model, Writer writer, boolean cacheable);

    /**
     * 清理缓存
     */
    void clearCache();

}

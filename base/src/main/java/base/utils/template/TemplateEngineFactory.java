package base.utils.template;

import base.utils.template.engines.FreeMarkerTemplateEngine;
import base.utils.template.engines.GroovyTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/6/5.
 * 模板引擎工厂
 */
public class TemplateEngineFactory {

    private static Map<String, TemplateEngine> engineMap = new HashMap<String, TemplateEngine>();

    private TemplateEngineFactory() {
    }

    /**
     * 根据类模板型获取模板引擎
     */
    public static TemplateEngine getEngine(String type) {
        return engineMap.get(type);
    }

    /**
     * 注册模板引擎
     */
    public static void registerEngine(String type, TemplateEngine engine) {
        engineMap.put(type, engine);
    }

    static {
        // 注册模板引擎
        registerEngine("groovy", new GroovyTemplateEngine());
        registerEngine("freemarker", new FreeMarkerTemplateEngine());
    }

}

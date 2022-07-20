package base.utils.ptu.handler;

import base.utils.ptu.PacketHandler;
import base.utils.ptu.exception.PacketException;
import base.utils.ptu.model.PacketConfig;
import base.utils.template.TemplateEngine;
import base.utils.template.TemplateEngineFactory;

import java.util.Map;

/**
 * Created by cl on 2017/4/18.
 * 模板XML报文处理器，通过模板生成XML报文
 */
public class TemplateXmlPacketHandler extends AbstractXmlPacketHandler implements PacketHandler {

    /* 报文模板类型 */
    private String templateType;

    @Override
    public String toPacket(PacketConfig config, Map<String, Object> params) {
        String xml = null;
        String tpl = config.getTemplate();
        try {
            TemplateEngine engine = TemplateEngineFactory.getEngine(templateType);
            xml = engine.evaluate(tpl, params);

            // 处理特殊转义字符
            xml = xml.replaceAll("\r", "")
                    .replaceAll("\n", "")
                    .replaceAll(">\\s+<", "><")
                    .replaceAll("&ldquo;", "“")
                    .replaceAll("&rdquo;", "”")
                    .replaceAll("&lsquo;", "‘")
                    .replaceAll("&rsquo;", "’");
        } catch (Exception e) {
            throw new PacketException(e);
        }

        return xml;
    }

    @Override
    public void init(Map<?, ?> initParams) {
        templateType = (String) initParams.get("ptu.template.type");
    }

}

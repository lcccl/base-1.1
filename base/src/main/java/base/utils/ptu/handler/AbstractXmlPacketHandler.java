package base.utils.ptu.handler;

import base.utils.ptu.PacketHandler;
import base.utils.ptu.exception.PacketException;
import base.utils.xml.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * Created by cl on 2017/4/18.
 * XML报文处理器基类
 */
public abstract class AbstractXmlPacketHandler implements PacketHandler {

    protected Log logger = LogFactory.getLog(AbstractXmlPacketHandler.class);

    /**
     * 报文解析默认实现，将xml报文解析成Map
     */
    @Override
    public Object fromPacket(String packet) {
        Map<String, Object> rs = null;

        if (StringUtils.isNotEmpty(packet)) {
            try {
                rs = XmlUtils.xml2Map(packet);
            } catch (Exception e) {
                throw new PacketException(e);
            }
        }

        return rs;
    }

    @Override
    public void init(Map<?, ?> initParams) {

    }

}

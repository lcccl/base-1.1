package base.utils.ptu.handler;

import base.utils.ptu.PacketHandler;
import base.utils.ptu.model.PacketConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by cl on 2017/6/19.
 * Form表单参数报文处理器
 */
public class FormPacketHandler implements PacketHandler {

    private final static Log logger = LogFactory.getLog(FormPacketHandler.class);

    @Override
    public String toPacket(PacketConfig config, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        String encode = config.getSendEncode();

        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (sb.length() > 0) {
                    sb.append("&");
                }

                sb.append(URLEncoder.encode(key, encode));
                if (null != value) {
                    sb.append("=").append(URLEncoder.encode(value.toString(), encode));
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return sb.toString();
    }

    @Override
    public Object fromPacket(String packet) {
        return packet;
    }

    @Override
    public void init(Map<?, ?> initParams) {
    }

}

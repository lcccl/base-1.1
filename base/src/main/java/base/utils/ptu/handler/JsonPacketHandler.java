package base.utils.ptu.handler;

import base.utils.ptu.PacketHandler;
import base.utils.ptu.model.PacketConfig;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by cl on 2017/6/9.
 * JSON报文处理器，生成JSON报文
 */
public class JsonPacketHandler implements PacketHandler {

    @Override
    public String toPacket(PacketConfig config, Map<String, Object> params) {
        return JSON.toJSONString(params);
    }

    @Override
    public Object fromPacket(String packet) {
        Object obj = null;

        if (StringUtils.isNotEmpty(packet)) {
            packet = packet.trim();
            if (packet.startsWith("{")) {
                obj = JSON.parseObject(packet);
            } else if (packet.startsWith("[")) {
                obj = JSON.parseArray(packet);
            }
        }

        return obj;
    }

    @Override
    public void init(Map<?, ?> initParams) {
    }

}

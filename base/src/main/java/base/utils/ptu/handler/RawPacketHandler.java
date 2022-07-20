package base.utils.ptu.handler;

import base.utils.ptu.PacketHandler;
import base.utils.ptu.model.PacketConfig;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by cl on 2017/6/30.
 * 原始的报文处理器，发送和返回的报文不做任何处理
 */
public class RawPacketHandler implements PacketHandler {

    @Override
    public String toPacket(PacketConfig config, Map<String, Object> params) {
        // 请求报文保存在params中（params中只有一个参数，参数名任意）
        Iterator<Object> it = params.values().iterator();
        return it.hasNext() ? it.next().toString() : "";
    }

    @Override
    public Object fromPacket(String packet) {
        return packet;
    }

    @Override
    public void init(Map<?, ?> initParams) {
    }

}

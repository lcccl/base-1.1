package base.utils.ptu;

import base.utils.ptu.model.PacketConfig;

import java.util.Map;

/**
 * Created by cl on 2017/4/14.
 * 报文处理器
 */
public interface PacketHandler {

    /**
     * 生成报文
     *
     * @param config 报文配置
     * @param params 参数
     * @return 报文字符串
     */
    String toPacket(PacketConfig config, Map<String, Object> params);

    /**
     * 解析报文
     *
     * @param packet 报文
     * @return 解析报文得到的对象
     */
    Object fromPacket(String packet);

    /**
     * 初始化
     *
     * @param initParams 初始化参数
     */
    void init(Map<?, ?> initParams);

}

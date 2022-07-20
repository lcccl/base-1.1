package base.utils.ptu;

import base.utils.ptu.model.PacketConfig;
import base.utils.ptu.model.PacketResult;

import java.util.Map;

/**
 * Created by cl on 2017/4/14.
 * 报文传输器
 */
public interface PacketTransfer {

    /**
     * 初始化
     *
     * @param initParams 初始化参数
     */
    void init(Map<?, ?> initParams);

    /**
     * 传输报文
     *
     * @param config 报文配置
     * @param packet 报文
     * @return 返回结果
     */
    PacketResult transport(PacketConfig config, String packet);

}

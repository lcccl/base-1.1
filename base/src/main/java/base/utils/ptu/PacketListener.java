package base.utils.ptu;

import base.utils.ptu.model.PacketResult;

/**
 * Created by cl on 2017/4/14.
 * 报文传输监听器
 */
public interface PacketListener {

    /**
     * 报文发送前
     *
     * @param sendPacket 发送报文
     * @return
     */
    String beforeSend(String sendPacket);

    /**
     * 报文发送后
     *
     * @param backPacket 返回报文
     * @return
     */
    String afterSend(String backPacket);

}

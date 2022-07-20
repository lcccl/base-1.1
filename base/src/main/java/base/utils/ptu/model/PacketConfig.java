package base.utils.ptu.model;


import base.utils.ptu.PacketListener;

/**
 * Created by cl on 2017/4/14.
 * 报文配置
 */
public class PacketConfig {
    /**
     * 报文处理器
     */
    private String packetHandler;

    /**
     * 报文传输器
     */
    private String packetTransfer;

    /**
     * 发送报文编码
     */
    private String sendEncode;

    /**
     * 返回报文编码
     */
    private String backEncode;

    /**
     * 请求url
     */
    private String url;

    /**
     * 扩展配置，以JSON的形式保存
     */
    private String extConfig;

    /**
     * 模板
     */
    private String template;

    /**
     * 监听器
     */
    private PacketListener listener;

    public String getPacketHandler() {
        return packetHandler;
    }

    public void setPacketHandler(String packetHandler) {
        this.packetHandler = packetHandler;
    }

    public String getPacketTransfer() {
        return packetTransfer;
    }

    public void setPacketTransfer(String packetTransfer) {
        this.packetTransfer = packetTransfer;
    }

    public String getSendEncode() {
        return sendEncode;
    }

    public void setSendEncode(String sendEncode) {
        this.sendEncode = sendEncode;
    }

    public String getBackEncode() {
        return backEncode;
    }

    public void setBackEncode(String backEncode) {
        this.backEncode = backEncode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtConfig() {
        return extConfig;
    }

    public void setExtConfig(String extConfig) {
        this.extConfig = extConfig;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public PacketListener getListener() {
        return listener;
    }

    public void setListener(PacketListener listener) {
        this.listener = listener;
    }
}

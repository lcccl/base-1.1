package base.utils.ptu.model;

import java.util.Map;

/**
 * Created by cl on 2017/4/14.
 * 报文请求返回结果
 */
public class PacketResult {
    /**
     * 发送报文
     */
    private String sendPacket;

    /**
     * 返回报文
     */
    private String backPacket;

    /**
     * 报文解析的数据
     */
    private Object data;

    /**
     * 请求状态
     */
    private String status;

    /**
     * 请求异常信息
     */
    private String errorMessage;

    public String getSendPacket() {
        return sendPacket;
    }

    public void setSendPacket(String sendPacket) {
        this.sendPacket = sendPacket;
    }

    public String getBackPacket() {
        return backPacket;
    }

    public void setBackPacket(String backPacket) {
        this.backPacket = backPacket;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

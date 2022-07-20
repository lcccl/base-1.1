package base.utils.ptu.transfer;

import base.utils.ptu.PacketTransfer;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * Created by cl on 2017/6/19.
 * Http表单请求传输器
 */
public class HttpFormPacketTransfer extends HttpPacketTransfer implements PacketTransfer {

    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";

    @Override
    protected HttpEntity createEntity(String packet, String encode, JSONObject extConfig) throws Exception {
        return new StringEntity(packet, ContentType.create(CONTENT_TYPE, encode));
    }

}

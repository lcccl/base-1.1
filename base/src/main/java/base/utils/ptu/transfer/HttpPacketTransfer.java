package base.utils.ptu.transfer;

import base.utils.ptu.PacketTransfer;
import base.utils.ptu.exception.PacketException;
import base.utils.ptu.model.PacketConfig;
import base.utils.ptu.model.PacketResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Created by cl on 2017/4/18.
 * Http请求报文传输器
 */
public class HttpPacketTransfer implements PacketTransfer {

    private HttpClient client = HttpClientBuilder.create().build();

    /* 连接超时时间 */
    protected int connTimeout;

    /* 请求超时时间 */
    protected int requestTimeout;

    @Override
    public void init(Map<?, ?> initParams) {
        // 初始化http传输参数
        String connTimeoutStr = (String) initParams.get("ptu.http.connTimeout");
        String requestTimeoutStr = (String) initParams.get("ptu.http.requestTimeout");
        connTimeout = null != connTimeoutStr ? Integer.valueOf(connTimeoutStr) : 30000;
        requestTimeout = null != requestTimeoutStr ? Integer.valueOf(requestTimeoutStr) : 30000;
    }

    @Override
    public PacketResult transport(PacketConfig config, String packet) {
        PacketResult rs = new PacketResult();

        // http请求参数
        String url = config.getUrl();
        String sendEncode = config.getSendEncode();
        String backEncode = config.getBackEncode();
        // 扩展配置
        String jsonStr = config.getExtConfig();
        jsonStr = StringUtils.isEmpty(jsonStr) ? "{}" : jsonStr;
        JSONObject extConfig = JSON.parseObject(jsonStr);

        try {
            // 创建请求配置
            RequestConfig requestConfig = this.createRequestConfig(extConfig);

            URI uri = new URI(url.trim());
            HttpPost post = new HttpPost(uri);
            post.setConfig(requestConfig);
            post.setEntity(this.createEntity(packet, sendEncode, extConfig));
            // 定制POST请求
            this.customizePost(post, extConfig);

            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            rs.setStatus(String.valueOf(statusCode));

            InputStream in = null;
            try {
                in = response.getEntity().getContent();
                String backPacket = IOUtils.toString(in, backEncode);
                rs.setBackPacket(backPacket);
            } finally {
                IOUtils.closeQuietly(in);
            }
        } catch (Exception e) {
            throw new PacketException(e);
        }

        return rs;
    }

    /**
     * 创建请求配置
     */
    protected RequestConfig createRequestConfig(JSONObject extConfig) {
        // 请求连接配置
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(connTimeout);
        builder.setSocketTimeout(requestTimeout);
        // 定制请求配置的builder
        this.customizeBuilder(builder, extConfig);

        return builder.build();
    }

    /**
     * 定制请求配置的builder
     */
    protected void customizeBuilder(RequestConfig.Builder builder, JSONObject extConfig) {
        // 设置代理
        JSONObject proxyConfig = extConfig.getJSONObject("proxy");
        if (null != proxyConfig) {
            String host = proxyConfig.getString("host");
            int port = proxyConfig.getIntValue("port");
            HttpHost proxy = new HttpHost(host, port);
            builder.setProxy(proxy);
        }
    }

    /**
     * 创建请求体
     */
    protected HttpEntity createEntity(String packet, String encode, JSONObject extConfig) throws Exception {
        String contentType = extConfig.getString("contentType");
        if (null != contentType) {
            return new StringEntity(packet, ContentType.create(contentType, encode));
        } else {
            return new StringEntity(packet, encode);
        }
    }

    /**
     * 定制POST请求
     */
    protected void customizePost(HttpPost post, JSONObject extConfig) {
        // 默认实现根据header节点，往HTTP Header中添加参数
        JSONObject headerConfig = extConfig.getJSONObject("header");
        if (null != headerConfig) {
            for (Map.Entry<String, Object> entry : headerConfig.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                post.addHeader(key, value);
            }
        }
    }

}

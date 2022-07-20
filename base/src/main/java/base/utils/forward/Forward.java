package base.utils.forward;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by cl on 2017/6/30.
 * 请求转发配置
 */
public class Forward extends JSONObject {

    public Forward(JSONObject json) {
        super(json);
    }

    /**
     * 获取转发的url
     */
    public String getUrl() {
        return this.getString("url");
    }

    /**
     * 获取代理
     */
    public JSONObject getProxy() {
        return this.getJSONObject("proxy");
    }

    /**
     * 获取代理主机地址
     */
    public String getProxyHost() {
        JSONObject proxy = this.getProxy();
        return null != proxy ? proxy.getString("host") : null;
    }

    /**
     * 获取代理端口号
     */
    public Integer getProxyPort() {
        JSONObject proxy = this.getProxy();
        return null != proxy ? proxy.getInteger("port") : null;
    }

}

package base.utils.forward;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/7/1.
 * Http请求转发管理器
 */
public class HttpForwardManager {

    private static Log logger = LogFactory.getLog(HttpForwardManager.class);

    /* 请求转发配置 */
    private static Map<String, Forward> forwardMap = new HashMap<String, Forward>();

    /* 默认的请求转发器 */
    private static HttpForward defaultHttpForward = new DefaultHttpForward();

    /* 请求转发器 */
    private static HttpForward targetHttpForward = null;

    private HttpForwardManager() {
    }

    /**
     * 初始化，加载默认的配置forward.json
     */
    public static void init() {
        InputStream in = HttpForwardManager.class.getResourceAsStream("/forward.json");
        if (null != in) {
            init(in);
        } else {
            logger.error("找不到配置文件：classpath:forward.json");
        }
    }

    /**
     * 初始化，加载配置
     */
    public static void init(InputStream in) {
        StringWriter sw = new StringWriter();
        try {
            IOUtils.copy(new InputStreamReader(in), sw);

            String jsonStr = sw.toString();
            JSONObject json = JSON.parseObject(jsonStr);
            init(json);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 初始化，加载配置
     */
    public static void init(JSONObject json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            String key = entry.getKey();
            JSONObject value = (JSONObject) entry.getValue();
            Forward forward = new Forward(value);
            forwardMap.put(key, forward);
        }
    }

    /**
     * 设置请求转发器（提供扩展，使用自定义的HttpForward）
     */
    public static void setHttpForward(HttpForward httpForward) {
        targetHttpForward = httpForward;
    }

    /**
     * 请求转发，根据code匹配forward.json配置文件中的转发配置
     */
    public static void forward(HttpServletRequest req, HttpServletResponse resp, String code) {
        // 获取请求转发配置
        Forward forward = forwardMap.get(code);

        if (null != forward) {
            logger.info("Host: " + req.getRemoteAddr() + ", http request forward: " + forward.getUrl());

            // 未指定HttpForward时，使用默认的请求转发器defaultHttpForward
            HttpForward httpForward = (null != targetHttpForward) ? targetHttpForward : defaultHttpForward;
            httpForward.forward(req, resp, forward);
        } else {
            logger.error("找不到" + code + "对应的转发配置");
        }
    }

}

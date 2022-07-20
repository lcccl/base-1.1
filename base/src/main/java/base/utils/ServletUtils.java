package base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cl on 2017/9/25.
 * Servlet工具类
 */
public class ServletUtils {

    private static Map<String, String> browserMapping = new HashMap<String, String>();
    private static Map<String, String> osMapping = new HashMap<String, String>();

    private ServletUtils() {
    }

    /**
     * 获取客户端IP地址
     * <p/>
     * 一般情况直接通过getRemoteAddr()方法即可获取，但是经过nginx等反向代理后，不能获取到真实的IP；
     * 经过代理后，虽然无法直接获取客户端的IP，但是在请求转发的过程中，增加了X-Real-IP、X-Forwarded-For等头信息，用于跟踪真实IP
     */
    public static String getClientIP(HttpServletRequest req) {
        String[] headers = new String[]{
                "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
                "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
        };

        String ip = null;
        for (String h : headers) {
            String val = req.getHeader(h);
            if (null != val && !"".equals(val.trim()) && !"unknow".equalsIgnoreCase(val.trim())) {
                ip = val;
                break;
            }
        }

        if (null != ip) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            int idx = ip.indexOf(",");
            if (idx != -1) {
                ip = ip.substring(0, idx);
            }
        } else {
            ip = req.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 获取客户端浏览器版本
     */
    public static String getClientBrowser(HttpServletRequest req) {
        String ver = null, userAgent = req.getHeader("user-agent");

        if (null != userAgent && !"".equals(userAgent)) {
            for (Map.Entry<String, String> entry : browserMapping.entrySet()) {
                String key = entry.getKey();
                if (userAgent.contains(key)) {
                    ver = entry.getValue();
                }
            }
        }

        return ver;
    }

    /**
     * 获取客户端操作系统
     */
    public static String getClientOS(HttpServletRequest req) {
        String os = null, userAgent = req.getHeader("user-agent");

        if (null != userAgent && !"".equals(userAgent)) {
            for (Map.Entry<String, String> entry : osMapping.entrySet()) {
                String key = entry.getKey();
                if (userAgent.indexOf(key) != -1) {
                    os = entry.getValue();
                }
            }
        }

        return os;
    }

    /**
     * 判断是否为ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest req) {
        if ("XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为合法的URL
     */
    public static boolean isValidUrl(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            return false;
        }

        if (null == uri.getHost()) {
            return false;
        }
        String scheme = uri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            return false;
        }

        return true;
    }

    /**
     * 输出json
     */
    public static void writeJson(HttpServletResponse resp, String jsonStr) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonStr);
    }

    static {
        // 读取配置
        String jsonStr = CommonUtils.getClassPathText("base/utils/ServletUtils.json", "UTF-8");
        JSONObject json = JSON.parseObject(jsonStr);
        JSONObject browser = json.getJSONObject("browser"), os = json.getJSONObject("os");

        for (Map.Entry<String, Object> entry : browser.entrySet()) {
            browserMapping.put(entry.getKey(), (String) entry.getValue());
        }

        for (Map.Entry<String, Object> entry : os.entrySet()) {
            osMapping.put(entry.getKey(), (String) entry.getValue());
        }
    }

}

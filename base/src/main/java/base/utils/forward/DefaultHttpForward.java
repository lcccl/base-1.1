package base.utils.forward;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by cl on 2017/6/30.
 * Http请求转发器默认实现
 */
public class DefaultHttpForward implements HttpForward {

    private final static Log logger = LogFactory.getLog(DefaultHttpForward.class);

    protected String[] ignoreHeaders = new String[]{"Set-Cookie", "Date", "Transfer-Encoding", "Connection"};

    /**
     * 请求转发
     */
    @Override
    public void forward(HttpServletRequest req, HttpServletResponse resp, Forward forward) {
        // 创建Http连接
        HttpURLConnection conn = createConnection(forward, req);

        try {
            // 设置转发请求的基本信息
            setForwardRequest(req, conn);
            // 转发请求到目标地址
            transfer(req.getInputStream(), conn.getOutputStream());

            // 设置转发请求响应的基本信息
            setForwardResponse(resp, conn);
            // 请求响应返回转发的结果
            transfer(conn.getInputStream(), resp.getOutputStream());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            // 断开连接
            conn.disconnect();
        }
    }

    /**
     * 创建转发的Http连接
     */
    protected HttpURLConnection createConnection(Forward forward, HttpServletRequest req) {
        // 创建Http连接
        HttpURLConnection conn = null;

        try {
            String forwardUrl = forward.getUrl();
            if ("get".equalsIgnoreCase(req.getMethod())) {
                forwardUrl = forwardUrl + "?" + this.getParamString(req);
            }
            URL url = new URL(forwardUrl);

            if (null != forward.getProxy()) {
                String proxyHost = forward.getProxyHost();
                Integer proxyPort = forward.getProxyPort();
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        proxyHost, proxyPort));
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setConnectTimeout(30 * 1000);
            conn.setReadTimeout(30 * 1000);

            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return conn;
    }

    /**
     * 设置转发请求的基本信息
     */
    protected void setForwardRequest(HttpServletRequest req, HttpURLConnection conn) throws Exception {
        // 设置请求基本信息
        String method = req.getMethod();
        conn.setRequestMethod(method);

        // 设置请求的Header
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = req.getHeader(headerName);
            if (null != headerName && null != headerValue) {
                conn.setRequestProperty(headerName, headerValue);

                if (logger.isDebugEnabled()) {
                    logger.debug("设置Request Header参数：" + headerName + "=" + headerValue);
                }
            }
        }
    }

    /**
     * 设置转发请求响应的基本信息
     */
    protected void setForwardResponse(HttpServletResponse resp, HttpURLConnection conn) throws Exception {
        // 设置响应的header
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            String headerValue = null != headerValues && headerValues.size() > 0 ? headerValues.get(0) : null;
            if (null != headerName && null != headerValue
                    && (!containsKey(ignoreHeaders, headerName, true))) {
                resp.setHeader(headerName, headerValue);

                if (logger.isDebugEnabled()) {
                    logger.debug("设置Response Header参数：" + headerName + "=" + headerValues);
                }
            }
        }
    }

    /**
     * 获取请求参数
     */
    protected String getParamString(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<String> params = req.getParameterNames();
            while (params.hasMoreElements()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                String paramName = params.nextElement();
                String paramValue = req.getParameter(paramName);
                sb.append(paramName).append("=").append(URLEncoder.encode(paramValue, "UTF-8"));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return sb.toString();
    }

    /**
     * 通过IO流传输
     */
    protected void transfer(InputStream in, OutputStream out) {
        try {
            IOUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 判断字符串数组中是否包含指定字符串
     */
    protected boolean containsKey(String[] arr, String key, boolean ignoreCase) {
        for (String str : arr) {
            boolean equalsFlag = ignoreCase ? key.equalsIgnoreCase(str) : key.equals(str);
            if (equalsFlag) {
                return true;
            }
        }
        return false;
    }

}

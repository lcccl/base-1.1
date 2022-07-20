package base.framework.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cl on 2017/9/21.
 * Cross Origin Resource Sharing（跨域资源共享）Filter
 * <p/>
 * Access-Control-Allow-Origin：允许访问的客户端域名，例如：http://web.xxx.com，若为*，则表示从任意域都能访问，即不做任何限制。
 * Access-Control-Allow-Methods：允许访问的方法名，多个方法名用逗号分割，例如：GET,POST,PUT,DELETE,OPTIONS。
 * Access-Control-Allow-Credentials：是否允许请求带有验证信息，若要获取客户端域下的cookie时，需要将其设置为true。
 * Access-Control-Allow-Headers：允许服务端访问的客户端请求头，多个请求头用逗号分割，例如：Content-Type。
 * Access-Control-Expose-Headers：允许客户端访问的服务端响应头，多个响应头用逗号分割。
 */
public class CorsFilter implements Filter {

    private final static String HEADER_ALLOW_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    private final static String HEADER_ALLOW_HEADERS = "x-requested-with";
    private final static String HEADER_ALLOW_CREDENTIALS = "true";

    private String allowOrigin;
    private String allowMethods;
    private String allowHeaders;
    private String allowCredentials;
    private String exposeHeaders;
    private String maxAge;

    private List<String> allowOriginList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowOrigin = filterConfig.getInitParameter("allowOrigin");
        allowMethods = defaultString(filterConfig.getInitParameter("allowMethods"), HEADER_ALLOW_METHODS);
        allowHeaders = defaultString(filterConfig.getInitParameter("allowHeaders"), HEADER_ALLOW_HEADERS);
        allowCredentials = defaultString(filterConfig.getInitParameter("allowCredentials"), HEADER_ALLOW_CREDENTIALS);
        exposeHeaders = filterConfig.getInitParameter("exposeHeaders");
        maxAge = filterConfig.getInitParameter("maxAge");

        if (!isEmpty(allowOrigin)) {
            allowOriginList = Arrays.asList(allowOrigin.split(","));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // 当前请求的域不为空，且未配置允许访问的域列表或者允许访问的域列表中包含当前请求的域
        String origin = req.getHeader("Origin");
        if (null != origin && (null == allowOriginList || allowOriginList.contains(origin))) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }

        resp.setHeader("Access-Control-Allow-Methods", allowMethods);
        resp.setHeader("Access-Control-Allow-Headers", allowHeaders);
        resp.setHeader("Access-Control-Allow-Credentials", allowCredentials);

        if (!isEmpty(exposeHeaders)) {
            resp.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        }
        if (!isEmpty(maxAge)) {
            resp.setHeader("Access-Control-Max-Age", maxAge);
        }

        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }

    private boolean isEmpty(String str) {
        return null == str || "".equals(str.trim());
    }

    private String defaultString(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

}

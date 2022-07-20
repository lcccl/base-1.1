package base.framework.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Created by cl on 2018/9/5.
 * 拦截器注册Bean
 */
public class InterceptorRegistrationBean {

    private HandlerInterceptor interceptor;

    private String[] pathPatterns;

    private String[] excludePathPatterns;

    private int order;

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public String[] getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(String... pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public String[] getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(String... excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}

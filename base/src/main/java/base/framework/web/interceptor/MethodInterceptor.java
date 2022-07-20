package base.framework.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cl on 2018/9/5.
 * 方法拦截器，增强了前置拦截、后置拦截的功能
 */
public abstract class MethodInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /* 是否拦截当前请求标志 */
    private ThreadLocal<Boolean> matchedThreadLocal = new ThreadLocal<Boolean>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        matchedThreadLocal.set(true);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean flag = this.before(handlerMethod, request, response);
        if (!flag) {
            this.reset();
        }

        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        if (null == modelAndView) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        this.after(handlerMethod, modelAndView, request, response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        this.reset();
    }

    /**
     * 判断本次请求是否需要拦截
     */
    public boolean isMatched() {
        return null != matchedThreadLocal.get() && matchedThreadLocal.get();
    }

    /**
     * 重置本地线程变量
     */
    public void reset() {
        matchedThreadLocal.remove();
    }

    /**
     * 前置拦截
     */
    public abstract boolean before(HandlerMethod handlerMethod, HttpServletRequest request,
                                   HttpServletResponse response);

    /**
     * 后置拦截（返回视图时拦截）
     */
    public abstract void after(HandlerMethod handlerMethod, ModelAndView modelAndView,
                               HttpServletRequest request,
                               HttpServletResponse response);

    /**
     * 后置拦截（返回数据放在body中时拦截）
     */
    public abstract Object after(Object body,
                                 MethodParameter methodParameter,
                                 MediaType mediaType,
                                 Class<? extends HttpMessageConverter<?>> aClass,
                                 ServerHttpRequest request,
                                 ServerHttpResponse response);

}

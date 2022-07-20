package base.config;

import base.framework.web.interceptor.InterceptorRegistrationBean;
import base.framework.web.interceptor.MethodInterceptor;
import base.framework.web.interceptor.MethodInterceptorAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cl on 2018/9/6.
 */
@Configuration
@Import({
        MethodInterceptorAdvice.class
})
public class MethodInterceptorConfig {

    /**
     * 拦截器链头部增加拦截器，每次请求前重置MethodInterceptor中的本地线程变量；
     * 防止出现异常或其他特殊情况时，MethodInterceptor没能够在请求结束时重置本地线程变量，导致MethodInterceptorAdvice中不该处理的请求也去处理；
     */
    @Bean
    public InterceptorRegistrationBean interceptorRegistrationBean() {
        HandlerInterceptor interceptor = new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                for (HandlerInterceptor it : MvcAutoConfig.getInterceptors()) {
                    if (it instanceof MethodInterceptor) {
                        MethodInterceptor methodInterceptor = (MethodInterceptor) it;
                        methodInterceptor.reset();
                    }
                }
                return true;
            }
        };

        InterceptorRegistrationBean registrationBean = new InterceptorRegistrationBean();
        registrationBean.setInterceptor(interceptor);
        registrationBean.setPathPatterns("/**");
        registrationBean.setOrder(-9999);
        return registrationBean;
    }

}

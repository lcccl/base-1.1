package base.framework.web.interceptor;

import base.config.MvcAutoConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * Created by cl on 2018/9/5.
 * 方法拦截器配套的Advice
 */
@ControllerAdvice
@Configuration
public class MethodInterceptorAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        List<HandlerInterceptor> interceptors = MvcAutoConfig.getInterceptors();
        if (interceptors.size() > 0) {
            for (int len = interceptors.size(), i = len - 1; i >= 0; i--) {
                HandlerInterceptor it = interceptors.get(i);
                if (it instanceof MethodInterceptor) {
                    MethodInterceptor interceptor = (MethodInterceptor) it;
                    if (interceptor.isMatched()) {
                        o = interceptor.after(o, methodParameter, mediaType, aClass, request, response);
                    }
                }
            }
        }

        return o;
    }

}

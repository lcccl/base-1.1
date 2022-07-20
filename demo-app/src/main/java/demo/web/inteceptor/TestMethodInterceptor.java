package demo.web.inteceptor;

import base.framework.model.JsonResult;
import base.framework.web.interceptor.MethodInterceptor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cl on 2018/9/5.
 * 测试方法拦截器
 */
public class TestMethodInterceptor extends MethodInterceptor {

    @Override
    public boolean before(HandlerMethod handlerMethod, HttpServletRequest request, HttpServletResponse response) {
        logger.info("==========拦截方法：{}", handlerMethod.getMethod().getName());
        return true;
    }

    @Override
    public void after(HandlerMethod handlerMethod, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public Object after(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        if (null == body) {
            return null;
        }

        if (body instanceof String) {
            return body;
        }

        logger.info("==========处理方法返回数据：{}", methodParameter.getMethod().getName());

        return new JsonResult(body);
    }

}

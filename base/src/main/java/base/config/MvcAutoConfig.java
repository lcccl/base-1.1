package base.config;

import base.framework.web.interceptor.InterceptorRegistrationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cl on 2018/9/5.
 * SpringMVC自动配置Bean，用于自动加载配置Inteceptor和HandlerMethodArgumentResolver
 */
@Configuration
public class MvcAutoConfig {

    /* 拦截器链 */
    private static List<HandlerInterceptor> interceptors = new ArrayList<HandlerInterceptor>();

    @Autowired(required = false)
    private List<InterceptorRegistrationBean> interceptorRegistrationBeans;

    @Autowired(required = false)
    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers;

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                super.addArgumentResolvers(argumentResolvers);

                if (null != handlerMethodArgumentResolvers && handlerMethodArgumentResolvers.size() > 0) {
                    argumentResolvers.addAll(handlerMethodArgumentResolvers);
                }
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                super.addInterceptors(registry);

                if (null != interceptorRegistrationBeans && interceptorRegistrationBeans.size() > 0) {
                    // 拦截器排序
                    Collections.sort(interceptorRegistrationBeans, new Comparator<InterceptorRegistrationBean>() {
                        @Override
                        public int compare(InterceptorRegistrationBean o1, InterceptorRegistrationBean o2) {
                            return o1.getOrder() - o2.getOrder();
                        }
                    });

                    // 注册拦截器
                    for (InterceptorRegistrationBean registrationBean : interceptorRegistrationBeans) {
                        HandlerInterceptor interceptor = registrationBean.getInterceptor();
                        InterceptorRegistration it = registry.addInterceptor(interceptor);
                        if (null != registrationBean.getPathPatterns()) {
                            it.addPathPatterns(registrationBean.getPathPatterns());
                        }
                        if (null != registrationBean.getExcludePathPatterns()) {
                            it.excludePathPatterns(registrationBean.getExcludePathPatterns());
                        }

                        // 保存到拦截器链
                        interceptors.add(interceptor);
                    }
                }
            }
        };
    }

    /**
     * 获取拦截器链
     */
    public static List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }

}
